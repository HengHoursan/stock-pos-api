package com.example.stockpos.app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Stock POS API",
                version = "1.0",
                description = "API documentation for Stock POS System",
                contact = @Contact(
                        name = "Support",
                        email = "support@example.com"
                )
        ),
        servers = {
                @Server(
                        description = "Local Environment",
                        url = "http://localhost:8080"
                )
        },
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER,
        description = "Enter your JWT token (without 'Bearer ').\n" +
                      "Example: eyJhbGciOiJIUzI1NiJ9...\n" +
                      "The 'Bearer' prefix will be added automatically."
)
public class SwaggerConfig {
}
