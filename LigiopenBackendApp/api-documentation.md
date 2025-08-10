# LigiOpen API Documentation

## Overview
LigiOpen is a comprehensive football league management system for Kenya with authentication, club management, match tracking, and competition structure capabilities.

**Base URL**: `http://localhost:8080` (or configured port)

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
- **User Authentication**: Local signup/signin and Google OAuth2
- **JWT Token Management**: Access and refresh token support
- **Club Management**: Football club creation and management
- **Match Tracking**: Match scheduling and live tracking
- **Competition Structure**: Leagues, groups, knockout tournaments
- **Player Management**: Player profiles and transfers
- **Statistics**: Match and player performance tracking
- **File Management**: AWS S3 integration for media storage

## Technical Stack

- **Framework**: Spring Boot 3.4.0
- **Database**: PostgreSQL
- **Authentication**: Spring Security + JWT
- **File Storage**: AWS S3
- **Session Management**: Redis
- **Build Tool**: Maven