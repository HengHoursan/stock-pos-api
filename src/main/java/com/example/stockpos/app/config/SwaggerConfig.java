package com.example.stockpos.app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
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
                description = """
                        ### Authentication Guide
                        This API uses JWT (JSON Web Tokens) for security. 
                        1. Login via the `/api/v1/auth/login` endpoint.
                        2. Copy the `accessToken` from the response.
                        3. Click the **Authorize** button and paste the token."""
        ),
        servers = {
                @Server(description = "Local Dev", url = "http://localhost:8080"),
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
        description = """
                Please provide your JWT token to authenticate.
                **Instructions:**
                * Simply paste the token value (e.g. `eyJhbGci...`)
                * **Note:** The `Bearer` prefix is added automatically!"""
)
public class SwaggerConfig {
}
