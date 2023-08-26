package com.tech.virtualpower.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI baseOpenApi() {
        return new OpenAPI().info(new Info().title("Virtual Power API").version("0.0.1").description("Spring doc"));
    }
}
