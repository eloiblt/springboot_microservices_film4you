package fr.cpe.filmforyou.filmcore.config;

import fr.cpe.filmforyou.filmcore.config.convert.MongoStringToLocalDateConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

@Configuration
public class MongoConvertersConfig {

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(List.of(new MongoStringToLocalDateConverter()));
    }

}
