package fr.cpe.filmforyou.usercore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(
        scanBasePackages = {"fr.cpe.filmforyou"}
)
@EnableDiscoveryClient
public class UserCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserCoreApplication.class, args);
    }
}
