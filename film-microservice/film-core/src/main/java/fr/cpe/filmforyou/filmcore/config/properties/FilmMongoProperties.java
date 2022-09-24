package fr.cpe.filmforyou.filmcore.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class FilmMongoProperties {

    private String host;
    private Long port;
    private String username;
    private String password;
    private String database;

}
