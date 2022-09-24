package fr.cpe.filmforyou.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "fr.cpe.filmforyou")
public class FeignConfiguration {
}
