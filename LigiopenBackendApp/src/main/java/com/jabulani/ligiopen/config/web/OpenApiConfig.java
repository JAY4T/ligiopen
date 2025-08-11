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
                        .version("2.0.0")
                        .description("""
                                # LigiOpen - Comprehensive Football League Management System for Kenya
                                
                                A state-of-the-art football league management system designed specifically for Kenya's diverse football ecosystem.
                                From village grassroots teams to premier league clubs, LigiOpen provides a comprehensive platform.
                                
                                ## 🎯 Current Status: Sprint 2 Complete ✅
                                **Location & Club Management System fully implemented and operational**
                                
                                ## API Sections
                                
                                ### 🔐 Authentication & Authorization ✅
                                - JWT authentication with refresh tokens
                                - Google OAuth2 integration
                                - Email verification system
                                - Password reset functionality
                                
                                ### 👤 User Profile Management ✅
                                - Complete user profile CRUD operations
                                - Profile picture upload with Digital Ocean Spaces
                                - User preferences and settings management
                                - Account management features
                                
                                ### 🏛️ Club Management ✅ (NEW - Sprint 2)
                                - **Club Registration**: Grassroots and FKF club registration system
                                - **Dual Verification**: LigiOpen internal + FKF official verification
                                - **Profile Management**: Complete CRUD operations with media upload
                                - **Staff Management**: Owner, manager, and role-based permissions
                                - **User Relationships**: Club favorites and ownership tracking
                                - **Geographic Integration**: Kenya county-based organization
                                - **Media Management**: Logo and photo uploads via Digital Ocean Spaces
                                
                                ### 🌍 Location & Infrastructure ✅ (NEW - Sprint 2)
                                - **Kenyan Counties**: All 47 counties with regional data
                                - **Stadium Management**: Complete venue information system
                                - **Geographic Search**: Location-based club and venue discovery
                                - **Regional Organization**: Counties grouped by regions
                                
                                ### ⚽ Player Management (Sprint 3 - Coming Soon)
                                - Player profiles and statistics
                                - Transfer management system
                                - Club membership tracking
                                
                                ### 🏆 Competition Structure (Sprint 4 - Coming Soon)
                                - League and tournament creation
                                - Season management
                                - Knockout and group stage support
                                
                                ### 📅 Match Management (Sprint 5 - Coming Soon)
                                - Match scheduling and live updates
                                - Real-time score tracking
                                - Professional commentary system
                                
                                ## 🚀 Getting Started
                                
                                ### Step 1: Authentication
                                1. **Register**: Create account using `/api/v1/auth/signup`
                                2. **Login**: Authenticate using `/api/v1/auth/signin` - copy the JWT token from response
                                3. **Authorize in Swagger**: 
                                   - Click the 🔒 "Authorize" button at the top of this page
                                   - Paste your JWT token (without "Bearer " prefix)
                                   - Click "Authorize" to enable authenticated requests
                                
                                ### Step 2: Club Management (NEW)
                                1. **Register Club**: Use `/api/v1/clubs/register/grassroots` or `/api/v1/clubs/register/fkf`
                                2. **Update Profile**: Use `/api/v1/clubs/{clubId}` to update club information
                                3. **Upload Media**: Use `/api/v1/clubs/{clubId}/logo` for club branding
                                4. **Manage Staff**: Add managers via `/api/v1/clubs/{clubId}/staff/managers/{managerId}`
                                5. **User Relations**: Favorite clubs via `/api/v1/clubs/{clubId}/relationships/favorite`
                                
                                ### Step 3: Location Services (NEW)
                                1. **Browse Counties**: Get all counties via `/api/v1/counties`
                                2. **Find Stadiums**: Get county stadiums via `/api/v1/counties/{countyId}/stadiums`
                                3. **Geographic Search**: Search clubs by location and proximity
                                
                                ## 🌟 Sprint 2 Highlights
                                - **50+ New API Endpoints** across 4 major controller areas
                                - **Dual Verification System** for grassroots and professional clubs
                                - **Complete Kenyan Geographic Data** with all 47 counties
                                - **Advanced Search Capabilities** with location-based filtering
                                - **Role-based Permission System** for club management
                                - **Media Management Integration** with Digital Ocean Spaces
                                
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