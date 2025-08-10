# LigiOpen Database Schema Analysis

## Overview
LigiOpen is designed as a comprehensive football league management system for Kenya, covering everything from grassroots to premier league level. The database schema supports a complete ecosystem similar to MLS but tailored for Kenyan football.

## Database Schema Domains

### 1. User Management & Authentication
- **UserEntity**: Core user management with multiple authentication methods
  - Local authentication (username/email + password)
  - Google OAuth2 integration
  - Role-based access (USER, CLUB_ADMIN, CONTENT_REVIEW_ADMIN, SUPER_ADMIN)
  - Profile management with photos and preferences
- **UserSession**: Redis-backed session management
- **Relationships**: Users can own clubs, manage clubs, and favorite clubs

### 2. Club Management
- **Club**: Comprehensive club profiles
  - Club levels from GRASSROOTS to PREMIER_LEAGUE
  - FKF registration integration
  - Verification system with statuses
  - Social media integration, branding (logos, colors)
  - Contact information and website integration
- **ClubStaffMember**: Staff roles within clubs
- **FavoritedClub**: User-club favoriting system
- **Geographic Integration**: Linked to Kenyan counties

### 3. Player Management
- **Player**: Detailed player profiles
  - Kenyan-specific fields (National ID, FKF registration)
  - Physical attributes, positions, preferred foot
  - Emergency contacts, biographical information
  - Market value tracking
- **ClubMembership**: Player-club relationships with roles
- **PlayerTransfer**: Transfer history and tracking
- **ClubInvitation**: Invitation system for player recruitment

### 4. Competition Structure
- **Competition**: Multi-format competition system
  - Types: LEAGUE, CUP, TOURNAMENT, FRIENDLY
  - Formats: ROUND_ROBIN, GROUP_STAGE_KNOCKOUT, STRAIGHT_KNOCKOUT
  - Levels: PREMIER_LEAGUE to GRASSROOTS, COUNTY, NATIONAL
  - Gender and age category support
- **Season**: Season management with registration periods
- **Stage**: Multi-stage competition support
- **Group**: Group stage management
- **KnockoutBracket**: Tournament bracket system
- **Standings**: League table management
- **PromotionRelegationRule**: Automatic promotion/relegation

### 5. Match Management
- **Match**: Comprehensive match tracking
  - Real-time status updates (SCHEDULED → LIVE → COMPLETED)
  - Score tracking including penalties
  - Official assignments (referee, assistants)
  - Weather conditions, attendance
  - Live streaming and highlights integration
- **MatchEvent**: Real-time match events (goals, cards, substitutions)
- **MatchLineup**: Team lineups and formations
- **MatchCommentary**: Live match commentary

### 6. Statistics & Performance
- **PlayerStatistic**: Season-long comprehensive player statistics
  - Appearance stats (games, starts, minutes)
  - Goal stats (goals, assists, penalties)
  - Disciplinary records (yellow/red cards)
  - Performance metrics (passing accuracy, shot accuracy)
  - Defensive stats (tackles, interceptions, clearances)
  - Goalkeeper-specific stats (saves, clean sheets)
- **MatchPlayerStatistic**: Per-match player performance

### 7. Location & Infrastructure
- **County**: Kenyan county system for geographic organization
- **Stadium**: Stadium information with capacity and facilities
- Geographic hierarchies for organizing competitions

### 8. Media & File Management
- **File**: AWS S3 integration for file storage
- **Media**: Media asset management
- **FileType**: File classification system
- Support for photos, videos, documents

### 9. Communication & Notifications
- **Notification**: System-wide notification management
- User engagement and communication tools

### 10. System Administration
- **AuditLog**: System audit trail
- **SystemSettings**: Configuration management
- Administrative tools and monitoring

## Schema Strengths

1. **Comprehensive Coverage**: Covers all aspects of football management
2. **Kenyan Context**: FKF integration, county system, local requirements
3. **Scalability**: Supports grassroots to professional levels
4. **Real-time Features**: Live match tracking, commentary, events
5. **Statistics Integration**: Comprehensive player and match statistics
6. **Media Rich**: Full media management with AWS S3
7. **Role-based Access**: Proper security and permission system
8. **Competition Flexibility**: Supports various competition formats

## Potential Schema Improvements

### 1. Enhanced Location System
```sql
-- Add sub-county and constituency support
ALTER TABLE counties ADD COLUMN constituencies JSON;
ALTER TABLE stadiums ADD COLUMN sub_county VARCHAR(255);
ALTER TABLE stadiums ADD COLUMN gps_coordinates POINT;
```

### 2. Enhanced Player Profiles
```sql
-- Add more Kenyan-specific player data
ALTER TABLE players ADD COLUMN birth_certificate_number VARCHAR(255);
ALTER TABLE players ADD COLUMN kcse_certificate_number VARCHAR(255);
ALTER TABLE players ADD COLUMN guardian_name VARCHAR(255);
ALTER TABLE players ADD COLUMN guardian_phone VARCHAR(255);
```

### 3. Financial Management
```sql
-- Add financial tracking tables (missing from current schema)
CREATE TABLE player_contracts (
    id BIGINT PRIMARY KEY,
    player_id BIGINT REFERENCES players(id),
    club_id BIGINT REFERENCES clubs(id),
    contract_start DATE,
    contract_end DATE,
    salary_amount DECIMAL(10,2),
    currency VARCHAR(3) DEFAULT 'KES',
    contract_type ENUM('PERMANENT', 'LOAN', 'TRIAL')
);

CREATE TABLE club_finances (
    id BIGINT PRIMARY KEY,
    club_id BIGINT REFERENCES clubs(id),
    season_id BIGINT REFERENCES seasons(id),
    registration_fees DECIMAL(10,2),
    prize_money_received DECIMAL(10,2),
    sponsorship_income DECIMAL(10,2),
    expenses DECIMAL(10,2)
);
```

### 4. Enhanced Match Officials
```sql
-- Create dedicated officials table instead of string fields
CREATE TABLE match_officials (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    fkf_license_number VARCHAR(255),
    official_type ENUM('REFEREE', 'ASSISTANT_REFEREE', 'FOURTH_OFFICIAL'),
    county_id BIGINT REFERENCES counties(id),
    phone_number VARCHAR(20),
    email VARCHAR(255)
);
```

### 5. Fan Engagement
```sql
-- Add fan/supporter features
CREATE TABLE club_supporters (
    id BIGINT PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    club_id BIGINT REFERENCES clubs(id),
    membership_type ENUM('FREE', 'PREMIUM', 'VIP'),
    membership_start DATE,
    membership_expiry DATE
);

CREATE TABLE match_tickets (
    id BIGINT PRIMARY KEY,
    match_id BIGINT REFERENCES matches(id),
    user_id BIGINT REFERENCES users(id),
    seat_section VARCHAR(50),
    ticket_price DECIMAL(8,2),
    purchase_date TIMESTAMP
);
```

## Development Priority Assessment

The current schema is well-designed and comprehensive. The suggested improvements are enhancements rather than critical fixes, indicating a solid foundation for development.

## Next Steps

Based on this schema analysis, the development should proceed with:
1. Core API development for existing entities
2. Authentication and authorization implementation
3. Basic CRUD operations for all domains
4. Real-time features for match tracking
5. Statistics calculation and reporting
6. File upload and media management
7. Enhanced features and optimizations