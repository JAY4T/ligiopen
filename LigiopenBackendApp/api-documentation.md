# LigiOpen API Documentation

## Overview
LigiOpen is a comprehensive football league management system for Kenya designed specifically for the diverse Kenyan football ecosystem, from grassroots village teams to premier league clubs.

**Base URL**: `http://localhost:8000` (or configured port)
**Current Version**: v3.0.0 (Sprint 3 Complete)

## 🎯 Current Status: Sprint 3 Complete ✅
**Club & Player Management Systems fully implemented with 90+ API endpoints**

### Sprint 3 Achievements (COMPLETED)
- **40+ New API Endpoints** for comprehensive player management
- **Complete Player Management System** with registration, search, and profiles
- **Transfer Management Workflow** with 15+ dedicated transfer endpoints
- **Club Membership Tracking** with contract details and history
- **Player Invitation System** for club-to-player communications
- **Enhanced Google OAuth2** with custom URL structure (`/api/v1/oauth2/authorization/google`)
- **Advanced Player Search** by position, age, height, preferred foot, experience level
- **Transfer Analytics** with high-value transfer queries and club statistics
- **Professional DTO Layer** with comprehensive validation and mapping
- **Database Layer Enhancements** with 40+ new DAO operations

### API Endpoint Categories (90+ Total)
1. **Club Registration & Verification**: 12 endpoints (unified registration, FKF promotion, verification workflows)
2. **Club Profile Management**: 14 endpoints (CRUD, search, media, statistics)
3. **Club Relationships**: 11 endpoints (favorites, popularity, recommendations)
4. **Club Staff Management**: 12 endpoints (managers, invitations, ownership transfer)
5. **Player Management**: 25+ endpoints (registration, search, profiles, photos, memberships) ✅ NEW
6. **Transfer Management**: 15+ endpoints (complete transfer workflow, analytics, statistics) ✅ NEW
7. **Authentication & Users**: 8+ endpoints (signup, login, profile management, Google OAuth2) ✅ Enhanced
8. **Location Services**: Stadium and county integration

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

### Google OAuth2 Authentication ✅ Enhanced (Sprint 3)

#### 1. Google OAuth Initiation (NEW)
```http
GET /api/v1/oauth2/authorization/google
```

**Description**: Browser-only endpoint that initiates Google OAuth2 authentication flow.

**Usage Notes**:
- Cannot be tested directly in Postman - this is a browser redirect endpoint
- Copy the URL and paste it in your browser
- Manual browser testing required for OAuth flow

**OAuth Flow Process**:
1. User clicks this URL in browser (or frontend redirects here)
2. Redirects to Google for user authentication
3. User signs in with Google account
4. Google redirects back to our callback URL
5. Our server processes the OAuth response
6. User gets redirected to success/failure endpoint with tokens

#### 2. Google Auth Success Callback ✅ Enhanced
```http
GET /api/v1/auth/google/success?token={jwt_token}&refreshToken={refresh_token}&expiresIn={seconds}&userId={user_id}&email={email}&isNewUser={boolean}
```

**Query Parameters**:
- `token` (required): JWT access token
- `refreshToken` (optional): JWT refresh token
- `expiresIn` (optional): Token expiration time in seconds
- `userId` (optional): User's unique ID in the system
- `email` (optional): User's email from Google
- `isNewUser` (optional): Whether this is a newly created user

**Response Success (200)**:
```json
{
  "status": "success",
  "message": "Google authentication successful",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
    "message": "Google authentication successful",
    "expiresIn": 604800
  }
}
```

#### 3. Google Auth Failure Callback
```http
GET /api/v1/auth/google/failure
```

**Response Error (401)**:
```json
{
  "status": "failed",
  "message": "failed",
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

## ⚽ Player Management Endpoints ✅ NEW (Sprint 3)

### Player Registration & Profiles

#### 1. Register Player
```http
POST /api/v1/players/registration
Authorization: Bearer {jwt_token}
Content-Type: application/json
```

**Request Body**:
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "dateOfBirth": "1995-06-15",
  "primaryPosition": "MIDFIELDER",
  "secondaryPosition": "ATTACKING_MIDFIELDER",
  "preferredFoot": "RIGHT",
  "height": 175.5,
  "weight": 70.0,
  "phoneNumber": "+254712345678",
  "email": "john.player@example.com",
  "nationalId": "12345678",
  "fkfId": "FKF123456",
  "experienceLevel": "PROFESSIONAL",
  "emergencyContactName": "Jane Doe",
  "emergencyContactPhone": "+254787654321",
  "placeOfBirth": "Nairobi",
  "nationality": "Kenyan"
}
```

#### 2. Search Players with Advanced Filters
```http
GET /api/v1/players/search?position=MIDFIELDER&minAge=20&maxAge=30&minHeight=170&preferredFoot=RIGHT&experienceLevel=PROFESSIONAL&page=0&size=10
Authorization: Bearer {jwt_token}
```

**Query Parameters**:
- `position` (optional): Filter by primary position
- `minAge`/`maxAge` (optional): Age range filter
- `minHeight`/`maxHeight` (optional): Height range filter
- `preferredFoot` (optional): LEFT, RIGHT, BOTH
- `experienceLevel` (optional): BEGINNER, AMATEUR, SEMI_PROFESSIONAL, PROFESSIONAL
- `page`/`size` (optional): Pagination parameters

#### 3. Get Player Details
```http
GET /api/v1/players/{playerId}
Authorization: Bearer {jwt_token}
```

#### 4. Update Player Profile
```http
PUT /api/v1/players/{playerId}
Authorization: Bearer {jwt_token}
Content-Type: application/json
```

#### 5. Upload Player Photo
```http
POST /api/v1/players/{playerId}/photo
Authorization: Bearer {jwt_token}
Content-Type: multipart/form-data

Form Data:
- file: [image_file] (JPG, PNG, max 10MB)
```

### Club Membership Management

#### 1. Get Player Club Memberships
```http
GET /api/v1/players/{playerId}/club-memberships
Authorization: Bearer {jwt_token}
```

#### 2. Get Active Club Membership
```http
GET /api/v1/players/{playerId}/club-memberships/active
Authorization: Bearer {jwt_token}
```

#### 3. Get Club Membership History
```http
GET /api/v1/players/{playerId}/club-memberships/history
Authorization: Bearer {jwt_token}
```

### Player Invitation System

#### 1. Send Club Invitation
```http
POST /api/v1/players/invitations/{invitationId}/send
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
  "playerId": 1,
  "clubId": 2,
  "message": "We would like to invite you to join our club",
  "contractType": "PROFESSIONAL",
  "proposedSalary": 50000.00,
  "contractDuration": 24
}
```

#### 2. Accept Invitation
```http
PUT /api/v1/players/invitations/{invitationId}/accept
Authorization: Bearer {jwt_token}
```

#### 3. Reject Invitation
```http
PUT /api/v1/players/invitations/{invitationId}/reject
Authorization: Bearer {jwt_token}
```

## 🔄 Transfer Management Endpoints ✅ NEW (Sprint 3)

### Transfer Requests

#### 1. Create Transfer Request
```http
POST /api/v1/transfers
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
  "playerId": 1,
  "fromClubId": 1,
  "toClubId": 2,
  "transferType": "PERMANENT",
  "contractType": "PROFESSIONAL",
  "transferFee": 500000.00,
  "proposedSalary": 50000.00,
  "contractDuration": 24,
  "notes": "Urgent transfer needed for upcoming season"
}
```

#### 2. Get Transfer by ID
```http
GET /api/v1/transfers/{transferId}
Authorization: Bearer {jwt_token}
```

### Transfer Workflow

#### 1. Approve Transfer
```http
PUT /api/v1/transfers/{transferId}/approve
Authorization: Bearer {jwt_token}
```

#### 2. Reject Transfer
```http
PUT /api/v1/transfers/{transferId}/reject?reason=Budget constraints
Authorization: Bearer {jwt_token}
```

#### 3. Complete Transfer
```http
PUT /api/v1/transfers/{transferId}/complete
Authorization: Bearer {jwt_token}
```

#### 4. Cancel Transfer
```http
PUT /api/v1/transfers/{transferId}/cancel?reason=Player changed mind
Authorization: Bearer {jwt_token}
```

### Transfer Analytics

#### 1. Get High-Value Transfers
```http
GET /api/v1/transfers/high-value?minimumValue=1000000&limit=10
Authorization: Bearer {jwt_token}
```

#### 2. Get Recent Transfers
```http
GET /api/v1/transfers/recent?limit=20
Authorization: Bearer {jwt_token}
```

#### 3. Get Club Transfer Statistics
```http
GET /api/v1/transfers/clubs/{clubId}/statistics
Authorization: Bearer {jwt_token}
```

#### 4. Get Transfers by Date Range
```http
GET /api/v1/transfers/date-range?startDate=2024-01-01&endDate=2024-12-31&page=0&size=10
Authorization: Bearer {jwt_token}
```

### Club-Specific Transfer Management

#### 1. Get Pending Transfers for Club
```http
GET /api/v1/transfers/clubs/{clubId}/pending?page=0&size=10
Authorization: Bearer {jwt_token}
```

#### 2. Get Incoming Transfers
```http
GET /api/v1/transfers/clubs/{clubId}/incoming?page=0&size=10
Authorization: Bearer {jwt_token}
```

#### 3. Get Outgoing Transfers
```http
GET /api/v1/transfers/clubs/{clubId}/outgoing?page=0&size=10
Authorization: Bearer {jwt_token}
```

## Features Overview

The LigiOpen system includes:
- **User Authentication**: Local signup/signin and Google OAuth2 ✅ Enhanced
- **JWT Token Management**: Access and refresh token support ✅
- **Club Management**: Football club creation and management ✅ (Sprint 2)
- **Player Management**: Player profiles, search, and club memberships ✅ (Sprint 3)
- **Transfer Management**: Complete transfer workflow and analytics ✅ (Sprint 3)
- **Location & Infrastructure**: Kenya counties and stadium management ✅ (Sprint 2)
- **Match Tracking**: Match scheduling and live tracking (Sprint 5)
- **Competition Structure**: Leagues, groups, knockout tournaments (Sprint 4)
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