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
                        .description("Local Development Server"))
                .addServersItem(new Server()
                        .url("https://dev.ligiopen.com")
                        .description("Development Environment (dev-branch)"))
                .addServersItem(new Server()
                        .url("https://prod.ligiopen.com")
                        .description("Production Environment (main branch)"))
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
                                1. **Register**: Create account using `/api/v1/auth/signup`
                                2. **Login**: Authenticate using `/api/v1/auth/signin` - copy the JWT token from response
                                3. **Authorize in Swagger**: 
                                   - Click the 🔒 "Authorize" button at the top of this page
                                   - Paste your JWT token (without "Bearer " prefix)
                                   - Click "Authorize" to enable authenticated requests
                                4. **Test Protected Endpoints**: Now you can use User Profile Management endpoints
                                
                                ## Environment URLs
                                - **Local Development**: http://localhost:8000 (your local machine)
                                - **Dev Environment**: https://dev.ligiopen.com (dev-branch deployments)  
                                - **Production**: https://prod.ligiopen.com (main branch deployments)
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