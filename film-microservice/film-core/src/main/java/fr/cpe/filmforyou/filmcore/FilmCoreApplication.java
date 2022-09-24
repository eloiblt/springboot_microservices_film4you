package fr.cpe.filmforyou.filmcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(
        scanBasePackages = {"fr.cpe.filmforyou"},
        exclude = {DataSourceAutoConfiguration.class}
)
@EnableDiscoveryClient
@EnableMongoRepositories
public class FilmCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilmCoreApplication.class, args);
    }

}
