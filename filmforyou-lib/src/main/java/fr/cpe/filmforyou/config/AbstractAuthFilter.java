package fr.cpe.filmforyou.config;

import fr.cpe.filmforyou.userlib.dto.UserDTO;
import fr.cpe.filmforyou.userlib.webservice.UserWebService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class AbstractAuthFilter extends BasicAuthenticationFilter {

    private final UserWebService userWebService;

    public AbstractAuthFilter(AuthenticationManager authenticationManager, UserWebService userWebService) {
        super(authenticationManager);
        this.userWebService = userWebService;
    }

    /**
     * Filtre qui permet de récupérer l'utilisateur de la requête et le stocker dans le SecurityContext.
     *
     * @param req
     * @param res
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        // Récupération du header qui contient le token JWT.
        String token = req.getHeader("Authorization");

        // Si jamais il n'y a pas de token JWT on passe à la suite.
        if (token == null || token.trim().isEmpty()) {
            chain.doFilter(req, res);
            return;
        }

        token = token.replace("Bearer ", "");

        // Récupération de l'utilisateur et stockage dans le SecurityContext.
        UsernamePasswordAuthenticationToken authentication = getAuthentication(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    /**
     * Retourne une classe UsernamePasswordAuthenticationToken en fonction de la requête
     *
     * @param token
     * @return
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String token) {

        if (token != null) {
            UserDTO userDTO = this.getUserDTO(token);

            logger.info("Récupération de l'utilisateur : " + userDTO);

            if (userDTO != null) {
                // Création de l'objet UsernamePasswordAuthenticationToken.
                return new UsernamePasswordAuthenticationToken(userDTO, null, Collections.<GrantedAuthority>emptyList());
            }
            return null;
        }

        return null;
    }

    @Cacheable("getUserDTO")
    public UserDTO getUserDTO(String token) {
        return this.userWebService.getUser(token);
    }
}
