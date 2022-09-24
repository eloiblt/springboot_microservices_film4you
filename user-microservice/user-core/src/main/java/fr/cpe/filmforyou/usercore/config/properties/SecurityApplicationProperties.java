package fr.cpe.filmforyou.usercore.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security")
public class SecurityApplicationProperties {

    /**
     * Secret du token JWT
     */
    private String secret;

    /**
     * Le temps d'expiration du token
     */
    private long expirationTime;

    /**
     * Le prefix du token une fois inséré dans le header HTTP
     */
    private String tokenPrefix;

    /**
     * Nom du header HTTP qui contient le token.
     */
    private String headerString;

}
