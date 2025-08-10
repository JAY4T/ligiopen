package com.jabulani.ligiopen.config.web;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
                .addServersItem(new Server()
                        .url("http://localhost:8000")
                        .description("Development Server"))
                .addServersItem(new Server()
                        .url("https://api.ligiopen.com")
                        .description("Production Server"))
                .info(new Info()
                        .title("LigiOpen API")
                        .version("1.0.0")
                        .description("""
                                # LigiOpen - Comprehensive Football League Management System for Kenya
                                
                                A state-of-the-art football league management system designed specifically for Kenya's diverse football ecosystem.
                                From village grassroots teams to premier league clubs, LigiOpen provides a comprehensive platform.
                                
                                ## API Sections
                                
                                ### 🔐 Authentication & Authorization
                                - JWT authentication with refresh tokens
                                - Google OAuth2 integration
                                - Email verification system
                                - Password reset functionality
                                
                                ### 👤 User Profile Management
                                - Complete user profile CRUD operations
                                - Profile picture upload with Digital Ocean Spaces
                                - User preferences and settings management
                                - Account management features
                                
                                ### 🏛️ Club Management (Coming Soon)
                                - Club registration and verification
                                - FKF compliance tracking
                                - Staff and member management
                                
                                ### ⚽ Player Management (Coming Soon)
                                - Player profiles and statistics
                                - Transfer management system
                                - Club membership tracking
                                
                                ### 🏆 Competition Structure (Coming Soon)
                                - League and tournament creation
                                - Season management
                                - Knockout and group stage support
                                
                                ### 📅 Match Management (Coming Soon)
                                - Match scheduling and live updates
                                - Real-time score tracking
                                - Professional commentary system
                                
                                ## Getting Started
                                1. Register a new account using `/api/v1/auth/signup`
                                2. Verify your email and login using `/api/v1/auth/signin`
                                3. Use the JWT token in the Authorization header for protected endpoints
                                4. Manage your profile using the User Profile Management endpoints
                                """)
                        .contact(new Contact()
                                .name("LigiOpen Team")
                                .email("support@ligiopen.com")
                                .url("https://ligiopen.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT Authorization header using the Bearer scheme. Example: \"Authorization: Bearer {token}\"")));
    }
}