package fr.cpe.filmforyou.preferencecore.config;

import fr.cpe.filmforyou.config.AbstractWebConfig;
import fr.cpe.filmforyou.userlib.webservice.UserWebService;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig extends AbstractWebConfig {
    public WebConfig(UserWebService userWebService) {
        super(userWebService);
    }
}
