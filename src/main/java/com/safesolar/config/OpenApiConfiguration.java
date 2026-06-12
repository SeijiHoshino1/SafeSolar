package com.safesolar.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Bean
    OpenAPI safeSolarOpenApi() {
        return new OpenAPI().info(new Info().title("SafeSolar API").version("1.0.0")
                .description("API REST para monitoramento, rateio de creditos e alertas de microgeracao solar."));
    }
}
