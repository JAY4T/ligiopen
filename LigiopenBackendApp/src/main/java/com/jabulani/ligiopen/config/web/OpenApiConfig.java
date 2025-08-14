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
                        .version("3.0.0")
                        .description("""
                                # LigiOpen - Comprehensive Football League Management System for Kenya
                                
                                A state-of-the-art football league management system designed specifically for Kenya's diverse football ecosystem.
                                From village grassroots teams to premier league clubs, LigiOpen provides a comprehensive platform.
                                
                                ## 🎯 Current Status: Sprint 3 Complete ✅
                                **Club & Player Management Systems fully implemented and operational**
                                
                                ## API Sections
                                
                                ### 🔐 Authentication & Authorization ✅
                                - JWT authentication with refresh tokens
                                - **Enhanced Google OAuth2** with custom `/api/v1/oauth2/authorization/google` endpoint
                                - Email verification system
                                - Password reset functionality
                                
                                ### 👤 User Profile Management ✅
                                - Complete user profile CRUD operations
                                - Profile picture upload with Digital Ocean Spaces
                                - User preferences and settings management
                                - Account management features
                                
                                ### 🏛️ Club Management System ✅ (Sprint 2 Complete - 52+ Endpoints)
                                - **Unified Registration**: Single endpoint supporting both grassroots and FKF clubs
                                - **FKF Promotion**: Grassroots clubs can upgrade to official FKF status
                                - **Dual Verification**: LigiOpen internal + FKF official verification workflows
                                - **Profile Management**: Complete CRUD operations with advanced search
                                - **Staff Management**: Owner/Manager hierarchy with invitation system
                                - **User Relationships**: Club favorites with popularity tracking
                                - **Geographic Search**: County, region, and proximity-based discovery
                                - **Media Management**: Professional branding with Digital Ocean Spaces
                                
                                ### 🌍 Location & Infrastructure ✅ (Sprint 2)
                                - **Kenyan Counties**: All 47 counties with regional data
                                - **Stadium Management**: Complete venue information system
                                - **Geographic Search**: Location-based club and venue discovery
                                - **Regional Organization**: Counties grouped by regions
                                
                                ### ⚽ Player Management System ✅ (Sprint 3 Complete - 40+ Endpoints)
                                - **Player Registration**: Complete player profiles with photo management
                                - **Advanced Search**: Filter by position, age, height, preferred foot, experience
                                - **Club Membership**: Track player-club relationships with contract details
                                - **Transfer Management**: Complete transfer workflow (request, approve, complete)
                                - **Invitation System**: Club-to-player invitations with status tracking
                                - **Statistics & Analytics**: Player performance and transfer statistics
                                - **Media Integration**: Player photos with Digital Ocean Spaces
                                
                                ### 🔄 Transfer Management System ✅ (Sprint 3 Complete - 15+ Endpoints)
                                - **Transfer Requests**: Create and manage transfer requests
                                - **Approval Workflow**: Complete approval process with multiple stakeholders
                                - **Transfer Statistics**: High-value transfers and club analytics
                                - **Transfer History**: Complete audit trail of player movements
                                - **Contract Management**: Contract types and duration tracking
                                
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
                                
                                ### Step 2: Club Management (52+ Endpoints)
                                1. **Unified Registration**: Use `/api/v1/clubs/registration` for both grassroots and FKF clubs
                                2. **FKF Promotion**: Upgrade grassroots clubs via `/api/v1/clubs/registration/{clubId}/promote-to-fkf`
                                3. **Profile Management**: Update via `/api/v1/clubs/{clubId}` with comprehensive search
                                4. **Staff Management**: Add managers via `/api/v1/clubs/{clubId}/staff/managers/{managerId}`
                                5. **User Relations**: Favorite clubs via `/api/v1/clubs/{clubId}/favorite`
                                6. **Verification**: Submit for verification via `/api/v1/clubs/registration/{clubId}/submit-verification`
                                
                                ### Step 3: Player Management (40+ Endpoints) ✅ NEW
                                1. **Player Registration**: Register players via `/api/v1/players/registration`
                                2. **Advanced Search**: Filter players by position, age, height via `/api/v1/players/search`
                                3. **Photo Management**: Upload player photos via `/api/v1/players/{playerId}/photo`
                                4. **Club Membership**: Track memberships via `/api/v1/players/{playerId}/club-memberships`
                                5. **Invitations**: Send club invitations via `/api/v1/players/invitations/{invitationId}/send`
                                6. **Transfers**: Create transfers via `/api/v1/transfers` (15+ transfer endpoints available)
                                
                                ### Step 4: Location Services
                                1. **Browse Counties**: Get all counties via `/api/v1/counties`
                                2. **Find Stadiums**: Get county stadiums via `/api/v1/counties/{countyId}/stadiums`
                                3. **Geographic Search**: Search clubs by location and proximity
                                
                                ## 🌟 Sprint 3 Achievements (COMPLETED)
                                - **40+ New API Endpoints** for comprehensive player management
                                - **Complete Player Management System** with registration, search, and profiles
                                - **Transfer Management Workflow** with request, approval, and completion
                                - **Club Membership Tracking** with contract details and history
                                - **Player Invitation System** for club-to-player communications
                                - **Enhanced Google OAuth2** with custom URL structure (`/api/v1/oauth2/authorization/google`)
                                - **Advanced Player Search** by position, age, height, preferred foot, experience level
                                - **Transfer Analytics** with high-value transfer queries and club statistics
                                - **Professional DTO Layer** with comprehensive validation and mapping
                                - **Database Layer Enhancements** with 40+ new DAO operations
                                
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