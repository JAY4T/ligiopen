# LigiOpen - Comprehensive Football League Management System for Kenya

<div align="center">

![LigiOpen Logo](docs/images/ligiopen-logo.png)

**Revolutionizing Kenyan Football Management from Grassroots to Premier League**

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue.svg)](https://www.postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis-7+-red.svg)](https://redis.io/)
[![AWS S3](https://img.shields.io/badge/AWS-S3-yellow.svg)](https://aws.amazon.com/s3/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)](https://github.com/yourusername/ligiopen)

[🌐 Live Demo](https://api.ligiopen.com) • [📖 Documentation](https://docs.ligiopen.com) • [📱 Mobile App](https://app.ligiopen.com) • [🎯 Roadmap](sprint-plan.md)

</div>

---

## 🏆 Table of Contents

- [Overview](#-overview)
- [Key Features](#-key-features)
- [Architecture](#-architecture)
- [Technology Stack](#-technology-stack)
- [Quick Start](#-quick-start)
- [API Documentation](#-api-documentation)
- [Database Schema](#-database-schema)
- [Development](#-development)
- [Deployment](#-deployment)
- [Contributing](#-contributing)
- [License](#-license)
- [Support](#-support)

---

## 🌟 Overview

**LigiOpen** is a state-of-the-art football league management system designed specifically for Kenya's diverse football ecosystem. From village grassroots teams to premier league clubs, LigiOpen provides a comprehensive platform that rivals major international football management systems like MLS's official application.

### 🎯 Mission
To digitize and modernize Kenyan football by providing a unified platform that connects players, clubs, officials, fans, and administrators across all levels of the beautiful game.

### 🇰🇪 Kenyan Focus
- **FKF Compliance**: Registration numbers and compliance with Football Kenya Federation standards
- **County-based Organization**: Reflects Kenya's 47-county structure
- **Grassroots Support**: Special focus on community-level football development
- **Local Payment Integration**: M-Pesa and local banking systems
- **Swahili/English Support**: Multilingual platform for accessibility
- **Professional Content Team**: Employed scouts and content managers for match coverage

### 🌍 Vision
To become the definitive digital platform for African football management, starting with Kenya and expanding continent-wide.

---

## ✨ Key Features

### 🔐 Authentication & User Management
- **Multi-method Authentication**: Email/username + Google OAuth2
- **Role-based Access Control**: USER, CLUB_ADMIN, CONTENT_REVIEW_ADMIN, SUPER_ADMIN
- **JWT Security**: Access and refresh token implementation
- **Profile Management**: Complete user profiles with media support
- **Account Verification**: Email verification and account activation

### 🏛️ Club Management System ✅ (Sprint 2 COMPLETE - 52+ Endpoints)
- **Unified Registration**: Single endpoint supporting both grassroots and FKF clubs
- **FKF Promotion Workflow**: Grassroots clubs can upgrade to official FKF status
- **Comprehensive Club Profiles**: Full CRUD operations with advanced search capabilities
- **FKF Compliance**: Official registration number tracking and dual verification
- **Professional Staff Management**: Owner/Manager hierarchy with invitation system
- **Media Management**: Professional branding via Digital Ocean Spaces integration
- **Geographic Integration**: Kenya's 47-county system with proximity-based search
- **User Relationships**: Club favorites with popularity tracking and recommendations
- **Advanced Discovery**: Name, county, region, level, and location-based club search
- **Role-based Permissions**: Owner/Manager/Admin access control system

### ⚽ Player Management System ✅ (Sprint 3 COMPLETE - 40+ Endpoints)
- **Complete Player Registration**: Comprehensive player profiles with photo management
- **Advanced Search & Filtering**: Position, age, height, preferred foot, experience level
- **Club Membership Tracking**: Player-club relationships with contract details and history
- **Transfer Management Workflow**: Request, approval, completion, and analytics system
- **Invitation System**: Club-to-player recruitment with status tracking
- **Statistics & Analytics**: Player performance metrics and transfer statistics
- **Media Integration**: Professional player photos with Digital Ocean Spaces
- **Kenyan-specific Fields**: National ID, FKF registration, emergency contacts
- **Contract Management**: Contract types, durations, and salary tracking
- **Multi-club Support**: Track relationships across multiple clubs and time periods

### 🏆 Competition Structure
- **Multi-format Competitions**: Leagues, cups, tournaments, friendlies
- **Flexible Competition Types**:
  - **Round Robin**: Traditional league format
  - **Group Stage + Knockout**: World Cup style
  - **Straight Knockout**: Traditional cup format
  - **Mixed Format**: Custom combinations
- **Multi-level Support**: Premier League to grassroots competitions
- **Season Management**: Registration periods, team participation, rules
- **Automatic Standings**: Real-time league table calculations
- **Promotion/Relegation**: Automated tier management

### 📅 Match Management & Content Creation
- **Professional Match Coverage**: Employed scouts provide match data and coverage
- **Manual Content Management**: Expert content creators manage match information
- **Real-time Updates**: Scout-provided live score updates and match events
- **Match Status Management**: SCHEDULED → LIVE → HALF_TIME → COMPLETED
- **Event Recording**: Goals, cards, substitutions manually recorded by trained scouts
- **Expert Commentary**: Professional commentary by LigiOpen employed content team
- **Media Management**: Photo and video content uploaded by field scouts
- **Weather & Conditions**: On-site data collection by match scouts

### 📊 Statistics & Analytics
- **Player Statistics**: Comprehensive performance tracking
  - Appearance stats (games, starts, minutes played)
  - Goal stats (goals, assists, penalties)
  - Disciplinary records (yellow/red cards)
  - Performance metrics (passing/shooting accuracy)
  - Defensive stats (tackles, interceptions, clearances)
  - Goalkeeper stats (saves, clean sheets, goals conceded)
- **Team Analytics**: Head-to-head records, seasonal performance
- **Competition Analytics**: League tables, historical data, trends
- **Real-time Calculations**: Auto-updated statistics from match events

### 🏟️ Location & Infrastructure ✅ (NEW - Sprint 2)
- **Complete Kenya Integration**: All 47 counties with regional organization
- **Stadium Management**: Capacity, facilities, GPS coordinates, and availability
- **Geographic Search**: Haversine formula for location-based club and venue discovery
- **Regional Organization**: Counties grouped by regions (Central, Coast, Eastern, etc.)
- **Infrastructure Tracking**: Facility management, verification, and maintenance status
- **Major Venues**: Pre-populated with Kenya's key stadiums (Kasarani, Nyayo, etc.)

### 📱 Media & Content Management
- **AWS S3 Integration**: Scalable file storage and delivery
- **Image Optimization**: Automatic resizing and format optimization
- **Video Management**: Match highlights and promotional content
- **Document Storage**: Contracts, registration forms, official documents
- **Content Moderation**: Multi-level content review system

### 🔔 Real-time Features
- **Live Match Updates**: WebSocket-based real-time communication
- **Push Notifications**: Match reminders, team news, transfer alerts
- **Activity Feeds**: Social-style updates for clubs and competitions
- **Live Commentary**: Real-time match narration

### 💰 Financial Management
- **Registration Fees**: Competition and season fee tracking
- **Prize Money Distribution**: Automated award calculations
- **Sponsorship Management**: Sponsor tracking and recognition
- **Payment Integration**: M-Pesa and banking system connections
- **Financial Reporting**: Club and competition financial analytics

### 🎮 Fan Engagement
- **Club Following**: User-club relationship management
- **Social Features**: Comments, reactions, and community interaction
- **Virtual Ticketing**: Digital ticket sales and management
- **Fan Polls & Voting**: Community engagement features
- **News & Updates**: Club and competition news feeds

### 📰 Professional Content Management
- **Employed Scout Network**: Professional scouts deployed across Kenya for match coverage
- **Content Creation Team**: Expert writers and analysts for news, match reports, and commentary
- **Real-time Data Collection**: On-ground scouts provide live match updates and statistics
- **Photography & Videography**: Professional media teams capture match highlights and club content
- **Editorial Quality Control**: Multi-level content review and fact-checking processes
- **Local Language Support**: Content creation in both English and Swahili
- **County Coverage Network**: Scout presence in all 47 Kenyan counties for comprehensive coverage

### 🛡️ Security & Compliance
- **Data Protection**: GDPR-compliant data handling
- **Secure Authentication**: Multi-factor authentication support
- **Audit Logging**: Comprehensive system activity tracking
- **Role-based Permissions**: Granular access control
- **FKF Compliance**: Official federation requirement adherence

---

## 🏗️ Architecture

### System Architecture
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Web Client    │    │  Mobile Client  │    │  Admin Portal   │
│   (React/Vue)   │    │ (React Native)  │    │    (React)      │
└─────────┬───────┘    └─────────┬───────┘    └─────────┬───────┘
          │                      │                      │
          └──────────────────────┼──────────────────────┘
                                 │
                    ┌─────────────┴─────────────┐
                    │     API Gateway           │
                    │   (Spring Boot 3.4.0)    │
                    └─────────────┬─────────────┘
                                 │
                    ┌─────────────┴─────────────┐
                    │     Core Services         │
                    │ ┌─────────────────────┐   │
                    │ │  Authentication     │   │
                    │ │  Club Management    │   │
                    │ │  Player Management  │   │
                    │ │  Match Management   │   │
                    │ │  Statistics Engine  │   │
                    │ │  Notification Svc   │   │
                    │ └─────────────────────┘   │
                    └─────────────┬─────────────┘
                                 │
                    ┌─────────────┴─────────────┐
                    │     Data Layer            │
                    │ ┌─────────┐ ┌─────────┐   │
                    │ │PostgreSQL│ │  Redis  │   │
                    │ │(Primary) │ │(Cache)  │   │
                    │ └─────────┘ └─────────┘   │
                    │ ┌─────────┐ ┌─────────┐   │
                    │ │  AWS S3 │ │WebSocket│   │
                    │ │(Storage)│ │(Realtime)│  │
                    │ └─────────┘ └─────────┘   │
                    └───────────────────────────┘
```

### Domain Model
```
User ──┐
       ├── owns ──────────── Club ──── located in ──── County
       ├── manages ─────────── │
       └── favorites ─────────┘
                               │
Club ──────── has ──────── Player ──── participates in ──── Match
  │                          │                              │
  └── participates in ── Competition ──── has ──── Season ──┘
                            │                       │
                            └──── contains ──── Standing
```

---

## 💻 Technology Stack

### Backend Technologies
| Component | Technology | Version | Purpose |
|-----------|------------|---------|---------|
| **Framework** | Spring Boot | 3.4.0 | Core application framework |
| **Language** | Java | 17 | Primary programming language |
| **Database** | PostgreSQL | 15+ | Primary data storage |
| **Cache** | Redis | 7+ | Session management & caching |
| **Storage** | AWS S3 | - | File and media storage |
| **Authentication** | Spring Security + JWT | - | Security and authentication |
| **ORM** | Hibernate/JPA | - | Database object mapping |
| **Build Tool** | Maven | 3.8+ | Dependency management & build |

### Integration Technologies
| Component | Technology | Purpose |
|-----------|------------|---------|
| **API Documentation** | OpenAPI 3 + Swagger | Interactive API documentation |
| **Email Service** | SendGrid | Email notifications and verification |
| **OAuth2** | Google OAuth2 | Third-party authentication |
| **Real-time** | WebSocket | Live updates and notifications |
| **Payment** | M-Pesa API | Local payment processing |
| **Migration** | Flyway | Database version control |

### DevOps & Infrastructure
| Component | Technology | Purpose |
|-----------|------------|---------|
| **CI/CD** | GitHub Actions | Automated testing and deployment |
| **Deployment** | Ubuntu Digital Ocean Droplets | Direct production hosting |
| **Service Management** | Systemd | Application service management |
| **Reverse Proxy** | Nginx | Load balancing and SSL termination |
| **SSL** | Let's Encrypt | HTTPS encryption |
| **Process Management** | Linux systemd | Service lifecycle management |
| **Monitoring** | Application Insights | Performance monitoring |

---

## 🚀 Quick Start

### Prerequisites
Ensure you have the following installed:
- **Java 17+** - [Download OpenJDK](https://openjdk.java.net/)
- **Maven 3.8+** - [Installation Guide](https://maven.apache.org/install.html)
- **PostgreSQL 15+** - [Download PostgreSQL](https://www.postgresql.org/download/)
- **Redis 7+** - [Installation Guide](https://redis.io/docs/getting-started/installation/)
- **Git** - [Download Git](https://git-scm.com/downloads)

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/ligiopen.git
cd ligiopen
```

### 2. Environment Setup
Create a `.env` file in the root directory:
```bash
# Database Configuration
DB_ADDRESS=localhost
DB_NAME=ligiopen_db
DB_USERNAME=ligiopen_user
DB_PASSWORD=your_secure_password

# JWT Configuration
JWT_SECRET=your_super_secret_jwt_key_here_minimum_256_bits
JWT_EXPIRATION_MS=86400000

# Google OAuth2
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_SECRET=your_google_client_secret

# AWS S3 Configuration
AWS_ACCESS_KEY_ID=your_aws_access_key
AWS_SECRET_ACCESS_KEY=your_aws_secret_key
S3_REGION=us-east-1
S3_ENDPOINT=https://s3.amazonaws.com
S3_BUCKET_NAME=ligiopen-storage

# Email Configuration
SENDGRID_API_KEY=your_sendgrid_api_key

# Redis Configuration (default values)
REDIS_HOST=localhost
REDIS_PORT=6379
```

### 3. Database Setup
```bash
# Create PostgreSQL database
createdb -U postgres ligiopen_db

# Create database user
psql -U postgres -c "CREATE USER ligiopen_user WITH PASSWORD 'your_secure_password';"
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE ligiopen_db TO ligiopen_user;"
```

### 4. Build and Run
```bash
# Install dependencies
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8000`

### 5. Access API Documentation
Once running, access the interactive API documentation:
- **Swagger UI**: http://localhost:8000/swagger-ui/index.html
- **OpenAPI JSON**: http://localhost:8000/v3/api-docs
- **OpenAPI YAML**: http://localhost:8000/v3/api-docs.yaml

### 6. Test Authentication
Test the authentication endpoints:
```bash
# Register a new user
curl -X POST http://localhost:8000/api/v1/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "securePassword123",
    "firstName": "Test",
    "lastName": "User"
  }'

# Login
curl -X POST http://localhost:8000/api/v1/auth/signin \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "securePassword123"
  }'
```

---

## 📚 API Documentation

### Authentication Endpoints

#### Register User
```http
POST /api/v1/auth/signup
Content-Type: application/json

{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "securePassword123",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+254712345678"
}
```

#### Login User
```http
POST /api/v1/auth/signin
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "securePassword123"
}
```

#### Refresh Token
```http
POST /api/v1/auth/refresh
Content-Type: application/json

{
  "refreshToken": "your_refresh_token_here"
}
```

### User Profile Management Endpoints

#### Get Current User Profile
```http
GET /api/v1/users/profile
Authorization: Bearer your_jwt_token_here
```

#### Get User Profile by ID
```http
GET /api/v1/users/{userId}/profile
Authorization: Bearer your_jwt_token_here
```

#### Update Current User Profile
```http
PUT /api/v1/users/profile
Authorization: Bearer your_jwt_token_here
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe Updated",
  "phoneNumber": "+254712345678",
  "bio": "Updated bio information",
  "preferredLanguage": "en"
}
```

#### Upload Profile Picture
```http
POST /api/v1/users/profile/picture
Authorization: Bearer your_jwt_token_here
Content-Type: multipart/form-data

Form Data:
- file: [image_file] (JPG, PNG, max 10MB)
```

#### Remove Profile Picture
```http
DELETE /api/v1/users/profile/picture
Authorization: Bearer your_jwt_token_here
```

#### Delete Current User Profile
```http
DELETE /api/v1/users/profile
Authorization: Bearer your_jwt_token_here
```

### Player Management Endpoints ✅ NEW (Sprint 3)

#### Register Player
```http
POST /api/v1/players/registration
Authorization: Bearer your_jwt_token_here
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "dateOfBirth": "1995-06-15",
  "primaryPosition": "MIDFIELDER",
  "preferredFoot": "RIGHT",
  "height": 175.5,
  "weight": 70.0,
  "phoneNumber": "+254712345678",
  "email": "john.player@example.com",
  "nationalId": "12345678",
  "fkfId": "FKF123456",
  "experienceLevel": "PROFESSIONAL"
}
```

#### Search Players with Advanced Filters
```http
GET /api/v1/players/search?position=MIDFIELDER&minAge=20&maxAge=30&minHeight=170&preferredFoot=RIGHT&page=0&size=10
Authorization: Bearer your_jwt_token_here
```

#### Get Player Details
```http
GET /api/v1/players/{playerId}
Authorization: Bearer your_jwt_token_here
```

#### Upload Player Photo
```http
POST /api/v1/players/{playerId}/photo
Authorization: Bearer your_jwt_token_here
Content-Type: multipart/form-data

Form Data:
- file: [image_file] (JPG, PNG, max 10MB)
```

#### Get Player Club Memberships
```http
GET /api/v1/players/{playerId}/club-memberships
Authorization: Bearer your_jwt_token_here
```

### Transfer Management Endpoints ✅ NEW (Sprint 3)

#### Create Transfer Request
```http
POST /api/v1/transfers
Authorization: Bearer your_jwt_token_here
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
  "notes": "Urgent transfer for upcoming season"
}
```

#### Approve Transfer
```http
PUT /api/v1/transfers/{transferId}/approve
Authorization: Bearer your_jwt_token_here
```

#### Get High-Value Transfers
```http
GET /api/v1/transfers/high-value?minimumValue=1000000&limit=10
Authorization: Bearer your_jwt_token_here
```

#### Get Club Transfer Statistics
```http
GET /api/v1/transfers/clubs/{clubId}/statistics
Authorization: Bearer your_jwt_token_here
```

### Authentication Headers
For protected endpoints, include the JWT token:
```http
Authorization: Bearer your_jwt_token_here
```

### Response Format
All API responses follow a consistent format:

**Success Response:**
```json
{
  "status": "success",
  "message": "Operation completed successfully",
  "data": {
    // Response data here
  }
}
```

**Error Response:**
```json
{
  "status": "failed",
  "errors": {
    "field_name": "Error message"
  }
}
```

### 🧪 API Testing with Postman
We provide a comprehensive Postman collection for testing all APIs:

**Quick Start:**
1. Import `postman/LigiOpen-API-Collection.json` into Postman
2. Import `postman/LigiOpen-Development-Environment.json`
3. Select the development environment and start testing

**Automated Testing:**
```bash
cd postman
npm install
npm run test:dev
```

**Collection Features:**
- ✅ Complete API coverage with examples
- ✅ Automated authentication token management
- ✅ Comprehensive test scripts for validation
- ✅ Environment variables for easy switching
- ✅ Detailed documentation for each endpoint

For complete API documentation, visit: **http://localhost:8000/swagger-ui/index.html**

---

## 🗃️ Database Schema

### Core Entities Overview

| Domain | Entities | Purpose |
|--------|----------|---------|
| **User Management** | UserEntity, UserSession | Authentication and user profiles |
| **Club Management** | Club, ClubStaffMember, FavoritedClub | Club profiles and management |
| **Player Management** | Player, ClubMembership, PlayerTransfer, ClubInvitation | Player profiles and club relationships |
| **Competition** | Competition, Season, Stage, Group, KnockoutBracket, Standings | Tournament and league structure |
| **Match Management** | Match, MatchEvent, MatchLineup, MatchCommentary | Match scheduling and live tracking |
| **Statistics** | PlayerStatistic, MatchPlayerStatistic | Performance analytics |
| **Location** | County, Stadium | Geographic and venue management |
| **Media** | File, Media, FileType | File and media management |
| **System** | AuditLog, SystemSettings, Notification | System administration |

### Key Relationships

```sql
-- Users can own and manage multiple clubs
User 1:N Club (owner)
User M:N Club (managers)

-- Players belong to clubs through memberships
Player 1:N ClubMembership N:1 Club

-- Competitions have seasons, seasons have matches
Competition 1:N Season 1:N Match

-- Matches track events and statistics
Match 1:N MatchEvent
Match 1:N MatchPlayerStatistic

-- Geographic hierarchy
County 1:N Stadium
County 1:N Club
Stadium 1:N Match
```

### Database Schema Documentation
For detailed schema analysis, see: [Database Schema Analysis](database-schema-analysis.md)

---

## 🔧 Development

### Project Structure
```
src/main/java/com/jabulani/ligiopen/
├── config/                 # Configuration classes
│   ├── security/          # Security and JWT configuration
│   ├── integration/       # External service configurations
│   └── web/              # Web and API configuration
├── controller/            # REST API controllers
│   └── auth/             # Authentication controllers
├── dto/                  # Data Transfer Objects
│   ├── auth/             # Authentication DTOs
│   ├── user/             # User-related DTOs
│   └── response/         # Response wrapper DTOs
├── entity/               # JPA Entity classes
│   ├── user/             # User management entities
│   ├── club/             # Club management entities
│   ├── player/           # Player management entities
│   ├── competition/      # Competition structure entities
│   ├── match/            # Match management entities
│   ├── statistics/       # Statistics tracking entities
│   ├── location/         # Geographic entities
│   ├── file/             # File management entities
│   ├── notification/     # Notification entities
│   └── system/           # System administration entities
├── dao/                  # Data Access Objects
├── service/              # Business logic services
├── mapper/               # Entity-DTO mappers
└── LigiOpenApplication.java  # Main application class
```

### Development Workflow

#### 1. Create Feature Branch
```bash
git checkout -b feature/new-feature-name
```

#### 2. Development Standards
- Follow Spring Boot best practices
- Maintain test coverage >80%
- Document all public APIs
- Use consistent naming conventions
- Implement proper error handling

#### 3. Testing
```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify

# Generate test coverage report
mvn jacoco:report
```

#### 4. Code Quality Checks
```bash
# Run static analysis
mvn checkstyle:check

# Run security analysis
mvn org.owasp:dependency-check-maven:check
```

#### 5. Create Pull Request
- Ensure all tests pass
- Update documentation
- Request code review
- Merge after approval

### Environment Configuration

#### Development
```properties
# application-dev.properties
spring.profiles.active=dev
spring.jpa.hibernate.ddl-auto=update
logging.level.com.jabulani.ligiopen=DEBUG
```

#### Testing
```properties
# application-test.properties
spring.profiles.active=test
spring.jpa.hibernate.ddl-auto=create-drop
spring.datasource.url=jdbc:h2:mem:testdb
```

#### Production
```properties
# application-prod.properties
spring.profiles.active=prod
spring.jpa.hibernate.ddl-auto=validate
logging.level.root=WARN
```

---

## 🚢 Deployment

### Production Deployment (Ubuntu Digital Ocean Droplet)

#### 1. Server Setup
```bash
# Update system
sudo apt update && sudo apt upgrade -y

# Install Java 17
sudo apt install openjdk-17-jdk -y

# Install Maven
sudo apt install maven -y

# Install PostgreSQL
sudo apt install postgresql postgresql-contrib -y

# Install Redis
sudo apt install redis-server -y

# Install Nginx
sudo apt install nginx -y

# Create application user
sudo useradd -r -s /bin/false ligiopen
sudo mkdir -p /opt/ligiopen
sudo chown ligiopen:ligiopen /opt/ligiopen
```

#### 2. Database Setup
```bash
# Configure PostgreSQL
sudo -u postgres createdb ligiopen_db
sudo -u postgres createuser ligiopen_user
sudo -u postgres psql -c "ALTER USER ligiopen_user WITH PASSWORD 'secure_password';"
sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE ligiopen_db TO ligiopen_user;"

# Configure Redis
sudo systemctl enable redis-server
sudo systemctl start redis-server
```

#### 3. Application Deployment
```bash
# Clone repository to server
cd /opt/ligiopen
git clone https://github.com/yourusername/ligiopen.git .

# Set environment variables
sudo nano /opt/ligiopen/.env
# Add production environment variables

# Build application
mvn clean package -DskipTests

# Create systemd service
sudo nano /etc/systemd/system/ligiopen.service
```

#### 4. Systemd Service Configuration
```ini
[Unit]
Description=LigiOpen Spring Boot Application
After=network.target

[Service]
Type=simple
User=ligiopen
WorkingDirectory=/opt/ligiopen
ExecStart=/usr/bin/java -jar /opt/ligiopen/target/ligiopen-0.0.1-SNAPSHOT.jar
EnvironmentFile=/opt/ligiopen/.env
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

#### 5. Start Application Service
```bash
# Enable and start service
sudo systemctl daemon-reload
sudo systemctl enable ligiopen
sudo systemctl start ligiopen

# Check status
sudo systemctl status ligiopen
```

#### 6. Nginx Configuration
```nginx
# /etc/nginx/sites-available/ligiopen
server {
    listen 80;
    server_name api.ligiopen.com;
    
    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

#### 7. SSL Setup with Let's Encrypt
```bash
# Install Certbot
sudo apt install certbot python3-certbot-nginx -y

# Obtain SSL certificate
sudo certbot --nginx -d api.ligiopen.com

# Auto-renewal setup
sudo crontab -e
# Add: 0 12 * * * /usr/bin/certbot renew --quiet
```

### CI/CD Pipeline (GitHub Actions)

```yaml
# .github/workflows/deploy.yml
name: Deploy to Production

on:
  push:
    branches: [main]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'adopt'
      - name: Run tests
        run: mvn clean test
        
  deploy:
    needs: test
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Deploy to server
        uses: appleboy/ssh-action@v0.1.5
        with:
          host: ${{ secrets.HOST }}
          username: ${{ secrets.USERNAME }}
          key: ${{ secrets.SSH_KEY }}
          script: |
            cd /opt/ligiopen
            git pull origin main
            mvn clean package -DskipTests
            sudo systemctl restart ligiopen
```

---

## 🤝 Contributing

We welcome contributions from the community! Here's how you can help make LigiOpen better.

### Ways to Contribute
- 🐛 **Bug Reports**: Report issues and bugs
- 💡 **Feature Requests**: Suggest new features
- 🔧 **Code Contributions**: Submit pull requests
- 📖 **Documentation**: Improve documentation
- 🌍 **Translations**: Help with internationalization
- 🧪 **Testing**: Help with manual testing

### Getting Started

#### 1. Fork the Repository
```bash
# Fork on GitHub, then clone your fork
git clone https://github.com/yourusername/ligiopen.git
cd ligiopen
```

#### 2. Set Up Development Environment
Follow the [Quick Start](#-quick-start) guide to set up your development environment.

#### 3. Create Feature Branch
```bash
git checkout -b feature/your-feature-name
```

#### 4. Make Your Changes
- Write clean, maintainable code
- Follow existing code style and conventions
- Add tests for new functionality
- Update documentation as needed

#### 5. Test Your Changes
```bash
# Run all tests
mvn test

# Run integration tests
mvn verify

# Check test coverage
mvn jacoco:report
```

#### 6. Submit Pull Request
- Commit your changes with clear commit messages
- Push to your fork
- Create a pull request with detailed description
- Link any related issues

### Development Guidelines

#### Code Style
- Follow Java naming conventions
- Use meaningful variable and method names
- Keep methods focused and concise
- Add JavaDoc comments for public APIs
- Use proper exception handling

#### Commit Messages
Follow conventional commit format:
```
type(scope): brief description

Detailed explanation if needed

Fixes #123
```

Types: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`

#### Pull Request Template
```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing
- [ ] Unit tests pass
- [ ] Integration tests pass
- [ ] Manual testing completed

## Checklist
- [ ] Code follows style guidelines
- [ ] Self-review completed
- [ ] Documentation updated
- [ ] No new warnings introduced
```

### Community Guidelines
- Be respectful and inclusive
- Provide constructive feedback
- Help newcomers get started
- Follow the [Code of Conduct](CODE_OF_CONDUCT.md)

---

## 📋 Sprint Planning

LigiOpen follows an agile development approach with planned sprints:

### Current Status: Sprint 3 COMPLETE ✅ - Ready for Sprint 4
- ✅ **Sprint 1 Completed**: Foundation & Core Authentication (JWT, OAuth2, User Profiles)
- ✅ **Sprint 2 Completed**: Comprehensive Club Management System (52+ Endpoints)
  - ✅ **Unified Registration**: Single endpoint supporting both grassroots and FKF clubs
  - ✅ **FKF Promotion System**: Grassroots clubs can upgrade to official FKF status
  - ✅ **Dual Verification Workflows**: LigiOpen internal + FKF official verification
  - ✅ **Professional Staff Management**: Owner/Manager hierarchy with invitation system
  - ✅ **Complete Kenya Integration**: All 47 counties with regional organization
  - ✅ **Stadium Management**: Major venues and facility information
  - ✅ **User Relationships**: Club favorites with popularity tracking and recommendations
  - ✅ **Media Integration**: Professional branding with Digital Ocean Spaces
  - ✅ **Advanced Search**: Name, county, region, level, and proximity-based discovery
  - ✅ **Role-based Permissions**: Owner/Manager/Admin access control system
- ✅ **Sprint 3 Completed**: Comprehensive Player Management System (40+ Endpoints)
  - ✅ **Player Registration & Profiles**: Complete player management with photo support
  - ✅ **Advanced Player Search**: Filter by position, age, height, preferred foot, experience
  - ✅ **Transfer Management System**: Complete transfer workflow with 15+ dedicated endpoints
  - ✅ **Club Membership Tracking**: Player-club relationships with contract details
  - ✅ **Invitation System**: Club-to-player recruitment workflow with status tracking
  - ✅ **Enhanced Google OAuth2**: Custom URL structure (`/api/v1/oauth2/authorization/google`)
  - ✅ **Transfer Analytics**: High-value transfer queries and club statistics
  - ✅ **Professional DTO Layer**: Comprehensive validation and mapping system
  - ✅ **Database Layer Enhancement**: 40+ new DAO operations for player management
  - ✅ **Postman Collection v3.2**: Updated with all 40+ new player and transfer endpoints

### Next Phases
- **Sprint 4**: Competition Structure (Next - Weeks 10-13)
- **Sprint 5**: Match Management & Scheduling (Weeks 14-17)
- **Sprint 6**: Statistics & Performance Analytics (Weeks 18-20)
- **Sprint 7**: Fan Engagement & Social Features (Weeks 21-24)

For detailed sprint planning, see: [Sprint Plan](sprint-plan.md)

---

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2024 LigiOpen

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## 🆘 Support

### Getting Help

#### 🐛 Bug Reports
If you find a bug, please create an issue with:
- Clear description of the problem
- Steps to reproduce
- Expected vs actual behavior
- Environment details (OS, Java version, etc.)
- Screenshots/logs if applicable

#### 💡 Feature Requests  
For feature requests, please include:
- Clear description of the feature
- Use case and benefits
- Any design considerations
- Links to similar implementations (if any)

#### ❓ Questions & Discussions
- **GitHub Discussions**: For general questions and community discussions
- **Discord Server**: Real-time chat with the community
- **Stack Overflow**: Tag questions with `ligiopen`

### Contact Information
- **Project Lead**: [Your Name](mailto:lead@ligiopen.com)
- **Technical Lead**: [Tech Lead](mailto:tech@ligiopen.com)  
- **General Inquiries**: [hello@ligiopen.com](mailto:hello@ligiopen.com)

### Community Links
- **Website**: [https://ligiopen.com](https://ligiopen.com)
- **Documentation**: [https://docs.ligiopen.com](https://docs.ligiopen.com)
- **Blog**: [https://blog.ligiopen.com](https://blog.ligiopen.com)
- **Twitter**: [@LigiOpenKE](https://twitter.com/LigiOpenKE)
- **LinkedIn**: [LigiOpen](https://linkedin.com/company/ligiopen)

### Professional Services
For enterprise deployments, custom integrations, or professional support:
- **Email**: [enterprise@ligiopen.com](mailto:enterprise@ligiopen.com)
- **Phone**: +254 700 000 000

---

## 🙏 Acknowledgments

### Special Thanks
- **Football Kenya Federation (FKF)** for guidance on compliance requirements
- **Kenyan Football Community** for feedback and testing
- **Open Source Contributors** who make this project possible
- **Spring Boot Team** for the excellent framework
- **PostgreSQL Team** for the robust database system

### Inspiration
LigiOpen draws inspiration from successful football management systems worldwide while staying true to Kenya's unique football culture and needs.

### Dependencies
We stand on the shoulders of giants. See [pom.xml](pom.xml) for complete dependency list.

---

<div align="center">

**Built with ❤️ in Kenya for Kenyan Football**

*"Uniting Kenyan Football Through Technology"*

[⬆ Back to Top](#ligiopen---comprehensive-football-league-management-system-for-kenya)

</div>