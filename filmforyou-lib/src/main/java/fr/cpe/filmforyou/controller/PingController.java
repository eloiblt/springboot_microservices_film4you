package fr.cpe.filmforyou.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/public/ping")
public class PingController {

    private final Environment env;
    private final Logger logger = LoggerFactory.getLogger(PingController.class);

    public PingController(Environment env) {
        this.env = env;
    }

    @GetMapping
    @Operation(description = "Check if service is up and display local port")
    @ApiResponse(responseCode = "200", description = "Service is up")
    public String ping() {
        this.logger.info("[PUBLIC] Pong on service {} and port : {}", env.getProperty("spring.application.name"), env.getProperty("local.server.port"));
        return String.format("[PUBLIC] Pong on service %s and port : %s", env.getProperty("spring.application.name"), env.getProperty("local.server.port"));
    }
}
