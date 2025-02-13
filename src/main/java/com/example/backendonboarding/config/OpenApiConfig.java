package com.example.backendonboarding.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .components(apiComponents())
                .servers(apiServers());
    }

    private Info apiInfo() {
        return new Info()
                .title("Backend Onboarding API Document")
                .version("v0.0.1")
                .description("This is the API documentation for Backend Onboarding.")
                .contact(new Contact().name("Simhun Yook").email("yooksi53@gmail.com"));
    }

    private Components apiComponents() {
        return new Components()
                .addSecuritySchemes("Bearer Authentication",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"));
    }

    private List<Server> apiServers() {
        return List.of(
                new Server().url("http://localhost:8080").description("Local Server"),
                new Server().url("https://api.example.com").description("Production Server")
        );
    }
}
