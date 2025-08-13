# LigiOpen API Documentation

## Overview
LigiOpen is a comprehensive football league management system for Kenya designed specifically for the diverse Kenyan football ecosystem, from grassroots village teams to premier league clubs.

**Base URL**: `http://localhost:8000` (or configured port)
**Current Version**: v2.1.0 (Sprint 2 Complete)

## 🎯 Current Status: Sprint 2 Complete ✅
**Club Management System fully implemented with 52+ API endpoints**

### Sprint 2 Achievements (COMPLETED)
- **52+ API Endpoints** across 4 major controller areas
- **Unified Registration System** supporting both grassroots and FKF clubs in one endpoint
- **FKF Promotion Workflow** allowing grassroots clubs to upgrade to official status
- **Dual Verification System** with comprehensive LigiOpen + FKF workflows
- **Professional Staff Management** with invitation and role management system
- **Complete Kenyan Geographic Data** with all 47 counties and regional organization
- **Advanced Search Capabilities** with name, county, region, level, and proximity-based discovery
- **Role-based Permission System** with Owner/Manager/Admin hierarchy
- **Media Management Integration** with Digital Ocean Spaces for professional branding

### API Endpoint Categories
1. **Club Registration & Verification**: 12 endpoints (unified registration, FKF promotion, verification workflows)
2. **Club Profile Management**: 14 endpoints (CRUD, search, media, statistics)
3. **Club Relationships**: 11 endpoints (favorites, popularity, recommendations)
4. **Club Staff Management**: 12 endpoints (managers, invitations, ownership transfer)
5. **Authentication & Users**: 5+ endpoints (signup, login, profile management)
6. **Location Services**: Stadium and county integration (interface ready for Sprint 3)

## Authentication Endpoints

### Local Authentication

#### 1. User Registration
```http
POST /api/auth/v1/signup
```

**Request Body**:
```json
{
  "username": "string",
  "email": "string",
  "password": "string",
  "firstName": "string",
  "lastName": "string",
  "phoneNumber": "string"
}
```

**Response Success (200)**:
```json
{
  "status": "success",
  "message": "Account created",
  "data": {
    "id": "long",
    "username": "string",
    "email": "string",
    "firstName": "string",
    "lastName": "string",
    "phoneNumber": "string",
    "emailVerified": false,
    "accountEnabled": true,
    "createdAt": "timestamp",
    "updatedAt": "timestamp"
  }
}
```

**Response Error (400)**:
```json
{
  "status": "failed",
  "errors": {
    "email": "Error: Email is already in use!"
  }
}
```
or
```json
{
  "status": "failed",
  "errors": {
    "username": "Error: Username is already in use!"
  }
}
```

#### 2. User Login
```http
POST /api/auth/v1/signin
```

**Request Body**:
```json
{
  "email": "string",     // Optional - use either email or username
  "username": "string",  // Optional - use either email or username
  "password": "string"
}
```

**Response Success (200)**:
```json
{
  "status": "success",
  "message": "success",
  "data": {
    "accessToken": "string",
    "refreshToken": "string",
    "message": "Login successful",
    "tokenType": "Bearer",
    "expiresIn": 3600
  }
}
```

**Response Error (401)**:
```json
{
  "status": "failed",
  "errors": {
    "authentication": "Invalid credentials"
  }
}
```

**Response Error (400)**:
```json
{
  "status": "failed",
  "errors": {
    "credential": "Error: Either email or username must be provided!"
  }
}
```

#### 3. Token Refresh
```http
POST /api/auth/v1/refresh
```

**Request Body**:
```json
{
  "refreshToken": "string"
}
```

**Response Success (200)**:
```json
{
  "status": "success",
  "message": "success",
  "data": {
    "accessToken": "string",
    "refreshToken": "string",
    "message": "Token refreshed successfully",
    "tokenType": "Bearer",
    "expiresIn": 3600
  }
}
```

**Response Error (401)**:
```json
{
  "status": "failed",
  "errors": {
    "refreshToken": "Invalid or expired refresh token"
  }
}
```

**Response Error (400)**:
```json
{
  "status": "failed",
  "errors": {
    "refreshToken": "Refresh token is required"
  }
}
```

### Google OAuth2 Authentication

#### 1. Google Auth Success Callback
```http
GET /api/auth/google/success?token={jwt_token}&refreshToken={refresh_token}&expiresIn={seconds}
```

**Query Parameters**:
- `token` (required): JWT access token
- `refreshToken` (optional): JWT refresh token
- `expiresIn` (optional): Token expiration time in seconds

**Response Success (200)**:
```json
{
  "status": "success",
  "message": "success",
  "data": {
    "accessToken": "string",
    "refreshToken": "string",
    "message": "Google authentication successful",
    "tokenType": "Bearer",
    "expiresIn": 3600
  }
}
```

#### 2. Google Auth Failure Callback
```http
GET /api/auth/google/failure
```

**Response Error (401)**:
```json
{
  "status": "failed",
  "errors": {
    "authentication": "Google authentication failed"
  }
}
```

## Authentication Headers

For protected endpoints, include the JWT token in the Authorization header:

```http
Authorization: Bearer {jwt_token}
```

## Club Management Endpoints (NEW - Sprint 2)

### Club Registration & Verification

#### 1. Unified Club Registration (NEW)
```http
POST /api/v1/clubs/registration
```

**Description**: Single endpoint supporting both grassroots and FKF club registration.

**Request Body**:
```json
{
  "name": "Test Football Club",
  "shortName": "Test FC",
  "abbreviation": "TFC",
  "countyId": 1,
  "description": "A test football club",
  "contactEmail": "contact@testfc.com",
  "contactPhone": "+254700123456",
  "colors": "Blue and White",
  "founded": "2024-01-15",
  "websiteUrl": "https://testfc.com",
  "clubLevel": "GRASSROOTS",
  "isFkfRegistered": false,
  "fkfRegistrationNumber": "FKF-2024-001",
  "fkfRegistrationDate": "2024-08-01"
}
```

**Response Success (201)**:
```json
{
  "status": "success",
  "message": "Club registered successfully",
  "data": {
    "id": 1,
    "name": "Test Football Club",
    "shortName": "Test FC",
    "clubLevel": "GRASSROOTS",
    "ligiopenVerificationStatus": "PENDING",
    "fkfVerificationStatus": "NOT_APPLICABLE"
  }
}
```

#### 2. Promote Club to FKF (NEW)
```http
POST /api/v1/clubs/registration/{clubId}/promote-to-fkf
```

**Description**: Promote a grassroots club to FKF status with official registration.

**Request Body**:
```json
{
  "fkfRegistrationNumber": "FKF-2024-001",
  "fkfRegistrationDate": "2024-08-01",
  "newClubLevel": "DIVISION_3"
}
```

#### 3. Submit for LigiOpen Verification
```http
POST /api/v1/clubs/registration/{clubId}/submit-verification
```

#### 4. LigiOpen Verify Club (Admin Only)
```http
POST /api/v1/clubs/registration/{clubId}/ligiopen-verify
```

**Request Body**:
```json
{
  "verified": true,
  "notes": "Club verified successfully after review"
}
```

### Club Profile Management

#### 1. Get Club by ID
```http
GET /api/v1/clubs/{clubId}
```

#### 2. Update Club Profile
```http
PUT /api/v1/clubs/{clubId}
```

#### 3. Search Clubs
```http
GET /api/v1/clubs/search?query=Test&page=0&size=10
```

#### 4. Get Clubs by County
```http
GET /api/v1/clubs/county/{countyId}?page=0&size=20
```

#### 5. Get Clubs Near Location
```http
GET /api/v1/clubs/near?lat=-1.2921&lng=36.8219&radiusKm=50
```

### Club Relationships

#### 1. Favorite Club
```http
POST /api/v1/clubs/{clubId}/favorite
```

#### 2. Get My Favorite Clubs
```http
GET /api/v1/clubs/favorites/my
```

#### 3. Get Popular Clubs
```http
GET /api/v1/clubs/popular
```

### Club Staff Management

#### 1. Add Club Manager
```http
POST /api/v1/clubs/{clubId}/staff/managers/{managerId}
```

#### 2. Transfer Club Ownership
```http
POST /api/v1/clubs/{clubId}/staff/transfer-ownership/{newOwnerId}
```

#### 3. Invite Manager
```http
POST /api/v1/clubs/{clubId}/staff/managers/invite
```

**Request Body**:
```json
{
  "email": "manager@example.com",
  "role": "MANAGER",
  "message": "Welcome to our club management team!"
}
```

### Club Data Models

#### ClubRegistrationDto
```json
{
  "name": "string",
  "shortName": "string", 
  "abbreviation": "string",
  "countyId": "long",
  "description": "string",
  "contactEmail": "string",
  "contactPhone": "string",
  "colors": "string",
  "founded": "date",
  "websiteUrl": "string",
  "socialMediaLinks": "string",
  "homeStadiumId": "long",
  "clubLevel": "enum",
  "isFkfRegistered": "boolean",
  "fkfRegistrationNumber": "string",
  "fkfRegistrationDate": "date"
}
```

#### ClubDto
```json
{
  "id": "long",
  "name": "string",
  "shortName": "string",
  "abbreviation": "string",
  "clubLevel": "enum",
  "county": "CountyDto",
  "homeStadium": "StadiumDto",
  "owner": "UserDto",
  "managers": ["UserDto"],
  "ligiopenVerificationStatus": "enum",
  "fkfVerificationStatus": "enum",
  "isActive": "boolean",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

#### Club Levels
- GRASSROOTS
- DIVISION_3
- DIVISION_2
- DIVISION_1
- PREMIER_LEAGUE
- NATIONAL_TEAM
- WOMEN_PREMIER
- YOUTH_LEAGUE

## Data Models

### SignupRequestDto
```json
{
  "username": "string",
  "email": "string",
  "password": "string",
  "firstName": "string",
  "lastName": "string",
  "phoneNumber": "string"
}
```

### LoginRequestDto
```json
{
  "email": "string",      // Optional - either email or username required
  "username": "string",   // Optional - either email or username required
  "password": "string"
}
```

### TokenDto
```json
{
  "accessToken": "string",
  "refreshToken": "string",
  "message": "string",
  "tokenType": "Bearer",
  "expiresIn": "number"
}
```

### UserDto
```json
{
  "id": "long",
  "username": "string",
  "email": "string",
  "firstName": "string",
  "lastName": "string",
  "phoneNumber": "string",
  "bio": "string",
  "preferredLanguage": "string",
  "profilePictureId": "long",
  "emailVerified": "boolean",
  "accountEnabled": "boolean",
  "role": "string",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

## Error Response Format

All error responses follow this structure:

```json
{
  "status": "failed",
  "errors": {
    "field_name": "error_message"
  }
}
```

## Success Response Format

All success responses follow this structure:

```json
{
  "status": "success",
  "message": "string",
  "data": "object"
}
```

## Environment Configuration

The application uses the following environment variables:

### Database
- `DB_ADDRESS`: PostgreSQL database host
- `DB_NAME`: Database name
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password

### JWT Configuration
- `JWT_SECRET`: Secret key for JWT token signing
- `JWT_EXPIRATION_MS`: Token expiration time in milliseconds

### Google OAuth2
- `GOOGLE_CLIENT_ID`: Google OAuth2 client ID
- `GOOGLE_SECRET`: Google OAuth2 client secret

### AWS S3 (for file storage)
- `AWS_ACCESS_KEY_ID`: AWS access key
- `AWS_SECRET_ACCESS_KEY`: AWS secret key
- `S3_REGION`: S3 bucket region
- `S3_ENDPOINT`: S3 endpoint URL
- `S3_BUCKET_NAME`: S3 bucket name

## Features Overview

The LigiOpen system includes:
- **User Authentication**: Local signup/signin and Google OAuth2 ✅
- **JWT Token Management**: Access and refresh token support ✅
- **Club Management**: Football club creation and management ✅ (NEW - Sprint 2)
- **Location & Infrastructure**: Kenya counties and stadium management ✅ (NEW - Sprint 2)
- **Match Tracking**: Match scheduling and live tracking (Sprint 5)
- **Competition Structure**: Leagues, groups, knockout tournaments (Sprint 4)
- **Player Management**: Player profiles and transfers (Sprint 3)
- **Statistics**: Match and player performance tracking (Sprint 6)
- **File Management**: Digital Ocean Spaces integration for media storage ✅

## Technical Stack

- **Framework**: Spring Boot 3.4.0
- **Database**: PostgreSQL
- **Authentication**: Spring Security + JWT
- **File Storage**: Digital Ocean Spaces
- **Session Management**: Redis
- **Build Tool**: Maven

---

## 🏛️ Club Management Endpoints (NEW - Sprint 2)

### Club Registration

#### Register Grassroots Club
```http
POST /api/v1/clubs/register/grassroots
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
  "name": "Kibera United FC",
  "shortName": "KUFC",
  "description": "Community football club from Kibera",
  "foundedYear": 2019,
  "colors": "Green and White",
  "homeCountyId": 1,
  "city": "Nairobi",
  "town": "Kibera",
  "address": "Olympic Estate, Kibera",
  "phoneNumber": "+254712345678",
  "email": "info@kiberaunited.co.ke",
  "websiteUrl": "https://kiberaunited.co.ke",
  "socialMediaHandles": {
    "twitter": "@KiberaUnitedFC",
    "facebook": "KiberaUnitedFC",
    "instagram": "kiberaunited"
  },
  "clubType": "GRASSROOTS"
}
```

#### Register FKF Club
```http
POST /api/v1/clubs/register/fkf
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
  "name": "Gor Mahia FC",
  "shortName": "GM",
  "description": "Kenya's most successful football club",
  "foundedYear": 1968,
  "colors": "Green and White",
  "homeCountyId": 1,
  "phoneNumber": "+254711000000",
  "email": "info@gormahiafc.co.ke",
  "clubType": "PROFESSIONAL",
  "fkfRegistrationNumber": "FKF-2024-001",
  "fkfRegistrationDate": "2024-01-15",
  "league": "FKF Premier League",
  "tier": 1
}
```

### Club Profile Management

#### Get Club by ID
```http
GET /api/v1/clubs/{clubId}
```

#### Update Club Profile
```http
PUT /api/v1/clubs/{clubId}
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
  "name": "Updated Club Name",
  "description": "Updated description",
  "phoneNumber": "+254787654321",
  "email": "updated@email.com"
}
```

#### Upload Club Logo
```http
POST /api/v1/clubs/{clubId}/logo
Authorization: Bearer {jwt_token}
Content-Type: multipart/form-data

Form Data:
- file: [image_file] (JPG, PNG, max 10MB)
```

### Club Staff Management

#### Add Club Manager
```http
POST /api/v1/clubs/{clubId}/staff/managers/{managerId}
Authorization: Bearer {jwt_token}
```

#### Get Club Staff
```http
GET /api/v1/clubs/{clubId}/staff
```

#### Transfer Ownership
```http
POST /api/v1/clubs/{clubId}/staff/transfer-ownership/{newOwnerId}
Authorization: Bearer {jwt_token}
```

### Club Relationships

#### Favorite Club
```http
POST /api/v1/clubs/{clubId}/relationships/favorite
Authorization: Bearer {jwt_token}
```

#### Get User's Favorited Clubs
```http
GET /api/v1/clubs/relationships/favorites
Authorization: Bearer {jwt_token}
```

---

## 🌍 Location & Infrastructure Endpoints (NEW - Sprint 2)

### Counties

#### Get All Kenyan Counties
```http
GET /api/v1/counties
```

**Response includes all 47 counties organized by regions:**
- Central Region (6 counties)
- Coast Region (6 counties) 
- Eastern Region (8 counties)
- North Eastern Region (3 counties)
- Nyanza Region (6 counties)
- Rift Valley Region (14 counties)
- Western Region (4 counties)

#### Get County by ID
```http
GET /api/v1/counties/{countyId}
```

### Stadiums

#### Get Stadiums by County
```http
GET /api/v1/counties/{countyId}/stadiums
```

**Major Stadiums Include:**
- **Nairobi**: Kasarani Stadium (60,000), Nyayo Stadium (30,000)
- **Mombasa**: Mombasa Municipal Stadium (10,000)
- **Kisumu**: Moi Stadium Kisumu (35,000)
- **Nakuru**: Afraha Stadium (8,200)

---

## 🔐 Club Verification System (Sprint 2)

### Dual Verification Process
1. **LigiOpen Internal Verification**: PENDING → VERIFIED/REJECTED/SUSPENDED
2. **FKF Official Verification**: NOT_APPLICABLE → PENDING → VERIFIED/EXPIRED

### Club Types
- **GRASSROOTS**: Community clubs, FKF verification = NOT_APPLICABLE
- **PROFESSIONAL**: Professional clubs, FKF verification required

---

**For complete interactive API documentation, visit:** http://localhost:8000/swagger-ui/index.html