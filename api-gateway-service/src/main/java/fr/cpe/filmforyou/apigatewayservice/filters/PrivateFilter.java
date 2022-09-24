package fr.cpe.filmforyou.apigatewayservice.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class PrivateFilter extends AbstractFilmForYouFilter implements GlobalFilter {

    private final Logger logger = LoggerFactory.getLogger(PrivateFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getPath().toString();

        if (path != null && path.contains("/private")) {
            logger.error("[PRIVATE] path is not allowed : {}", path);
            return onError(exchange, HttpStatus.FORBIDDEN);
        }

        return chain.filter(exchange);
    }

}
