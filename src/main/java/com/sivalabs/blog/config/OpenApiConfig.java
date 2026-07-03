package com.sivalabs.blog.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OpenApiConfig {
    private final String bearerAuthSchemeName = "bearerAuth";

    @Bean
    OpenAPI openApi() {
        return new OpenAPI()
            .info(info())
            .addSecurityItem(new SecurityRequirement().addList("Authorization"))
            .components(
                new Components()
                    .addSecuritySchemes(bearerAuthSchemeName, createBasicAuthScheme())
            );
    }

    private Info info() {
        Contact contact = new Contact().name("SivaLabs").email("support@sivalabs.in");
        return new Info()
                .title("Blog API")
                .description("Blog API Swagger Documentation")
                .version("v1.0.0")
                .contact(contact);
    }

    private SecurityScheme createBasicAuthScheme() {
        return new SecurityScheme()
                .name(bearerAuthSchemeName)
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}
