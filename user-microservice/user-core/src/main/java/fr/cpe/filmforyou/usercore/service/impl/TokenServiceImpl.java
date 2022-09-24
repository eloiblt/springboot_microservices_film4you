package fr.cpe.filmforyou.usercore.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cpe.filmforyou.exception.FilmForYouException;
import fr.cpe.filmforyou.usercore.config.properties.SecurityApplicationProperties;
import fr.cpe.filmforyou.usercore.service.TokenService;
import fr.cpe.filmforyou.userlib.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Service
public class TokenServiceImpl implements TokenService {

    private final Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);

    private final SecurityApplicationProperties securityApplicationProperties;

    public TokenServiceImpl(SecurityApplicationProperties securityApplicationProperties) {
        this.securityApplicationProperties = securityApplicationProperties;
    }

    public String getTokenFromUser(UserDTO userDTO) throws FilmForYouException {
        this.logger.info("Generate token for user {}", userDTO);

        try {
            String tokenSubject = new ObjectMapper().writeValueAsString(userDTO);

            return JWT.create()
                    // Ajout des données que l'on souhaite stocker que les Token.
                    // Dans l'exemple nous décidons de stoker uniquement le username de l'utilisateur.
                    .withSubject(tokenSubject)
                    // Ajout de la date d'expiration du token.
                    .withExpiresAt(new Date(System.currentTimeMillis() + securityApplicationProperties.getExpirationTime()))
                    // Ajout de la signature du token avec le secret.
                    .sign(HMAC512(securityApplicationProperties.getSecret().getBytes(StandardCharsets.UTF_8)));

        } catch (Exception e) {
            throw new FilmForYouException("Erreur lors du parsage de l'utilisateur");
        }
    }

    public UserDTO getUserFromToken(String token) throws FilmForYouException {
        this.logger.info("Decode user token : {}", token);

        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWT.require(Algorithm.HMAC512(securityApplicationProperties.getSecret().getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .verify(token);
        } catch (Exception e) {
            throw new FilmForYouException("Le token n'est pas valide");
        }

        try {
            return new ObjectMapper().readValue(decodedJWT.getSubject(), UserDTO.class);
        } catch (Exception e) {
            throw new FilmForYouException("Erreur lors de la récupération de l'utilisateur dans le token");
        }
    }

    @Override
    public Boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(securityApplicationProperties.getSecret().getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .verify(token);

            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

}
