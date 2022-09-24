package fr.cpe.filmforyou.apigatewayservice.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

//@Component
public class AuthFilter extends AbstractFilmForYouFilter implements GlobalFilter {

    private final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    private final WebClient.Builder webClientBuilder;

    public AuthFilter(WebClient.Builder webClientBuilder) {
        super();
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getPath().toString();

        if (path != null && path.contains("/secured")) {

            String authorizationValue = this.collectHeader(exchange, "Authorization");

            if (authorizationValue == null) {
                logger.error("[AUTH] header {} is not found on : {}", "Authorization", path);
                return onError(exchange, HttpStatus.FORBIDDEN);
            }

            Mono<Boolean> isValidMono = webClientBuilder.baseUrl("http://users").build().post().uri("/private/token/valid").body(Mono.just(authorizationValue.replace("Bearer ", "")), String.class).retrieve().bodyToMono(Boolean.class);

            return Mono.from(isValidMono).flatMap(isValid -> {
                if (isValid == null || isValid.equals(Boolean.FALSE)) {
                    logger.error("[AUTH] Token not valid by user-microservice on : {}", path);
                    return onError(exchange, HttpStatus.FORBIDDEN);
                }

                return chain.filter(exchange);
            });
        }

        return chain.filter(exchange);
    }

    private String collectHeader(ServerWebExchange exchange, String headerName) {
        HttpHeaders headers = exchange.getRequest().getHeaders();

        if (!headers.containsKey(headerName)) {
            return null;
        }

        List<String> values = headers.get(headerName);

        if (values == null || values.isEmpty()) {
            return null;
        }

        return values.get(0);
    }
}
