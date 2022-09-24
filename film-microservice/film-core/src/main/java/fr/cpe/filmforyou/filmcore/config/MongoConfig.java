package fr.cpe.filmforyou.filmcore.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import fr.cpe.filmforyou.filmcore.config.properties.FilmMongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

public class MongoConfig {

    private final FilmMongoProperties filmMongoProperties;

    public MongoConfig(FilmMongoProperties filmMongoProperties) {
        this.filmMongoProperties = filmMongoProperties;
    }

    @Bean
    public MongoClient mongo() {
        ConnectionString connectionString = new ConnectionString(String.format("mongodb://%s:%s@%s:%d/%s", this.filmMongoProperties.getUsername(), this.filmMongoProperties.getPassword(), this.filmMongoProperties.getHost(), this.filmMongoProperties.getPort(), this.filmMongoProperties.getDatabase()));
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient client) {
        return new MongoTemplate(client, this.filmMongoProperties.getDatabase());
    }

}
