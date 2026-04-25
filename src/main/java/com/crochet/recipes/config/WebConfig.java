package com.crochet.recipes.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origins:http://localhost:3000,http://localhost:5173}")
    private String allowedOrigins;

    @Value("${cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}")
    private String allowedMethods;

    @Value("${cors.allowed-headers:*}")
    private String allowedHeaders;

    @Value("${cors.max-age:3600}")
    private long maxAge;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] origins = allowedOrigins.split(",");
        String[] methods = allowedMethods.split(",");

        log.info("Configurando CORS para origins: {}", allowedOrigins);

        registry.addMapping("/api/**")
            .allowedOrigins(origins)
            .allowedMethods(methods)
            .allowedHeaders(allowedHeaders.split(","))
            .allowCredentials(true)
            .maxAge(maxAge);
    }
}

