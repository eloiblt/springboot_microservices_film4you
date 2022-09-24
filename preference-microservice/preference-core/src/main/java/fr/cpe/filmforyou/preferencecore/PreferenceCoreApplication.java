package fr.cpe.filmforyou.preferencecore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {"fr.cpe.filmforyou"}
)
public class PreferenceCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(PreferenceCoreApplication.class, args);
    }

}
