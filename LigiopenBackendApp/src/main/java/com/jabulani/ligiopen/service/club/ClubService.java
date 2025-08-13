package com.jabulani.ligiopen.service.club;

import com.jabulani.ligiopen.entity.club.Club;
import com.jabulani.ligiopen.entity.user.UserEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ClubService {
    
    /**
     * Register a new club (grassroots level by default)
     * @param name Club name
     * @param shortName Club short name (optional)
     * @param ownerId User ID of the club owner
     * @param countyId County ID where club is located
     * @param description Club description
     * @param contactEmail Contact email
     * @param contactPhone Contact phone
     * @return Created Club entity
     */
    Club registerClub(String name, String shortName, Long ownerId, Long countyId, 
                     String description, String contactEmail, String contactPhone);
    
    /**
     * Unified club registration method supporting both grassroots and FKF clubs
     * @param registrationDto Complete club registration details
     * @param ownerId User ID of the club owner
     * @return Created Club entity
     */
    Club registerClubUnified(com.jabulani.ligiopen.dto.club.ClubRegistrationDto registrationDto, Long ownerId);
    
    /**
     * Register a club with FKF registration number
     * @param name Club name
     * @param shortName Club short name (optional)
     * @param ownerId User ID of the club owner
     * @param countyId County ID where club is located
     * @param description Club description
     * @param contactEmail Contact email
     * @param contactPhone Contact phone
     * @param registrationNumber FKF registration number
     * @param clubLevel Club level
     * @param founded Foundation date
     * @return Created Club entity
     */
    Club registerClubWithFkfNumber(String name, String shortName, Long ownerId, Long countyId,
                                  String description, String contactEmail, String contactPhone,
                                  String registrationNumber, Club.ClubLevel clubLevel, LocalDate founded);
    
    /**
     * Update basic club information
     * @param clubId Club ID
     * @param userId User ID (must be owner or manager)
     * @param name Club name
     * @param shortName Club short name
     * @param description Club description
     * @param contactEmail Contact email
     * @param contactPhone Contact phone
     * @return Updated Club entity
     */
    Club updateClubInfo(Long clubId, Long userId, String name, String shortName, 
                       String description, String contactEmail, String contactPhone);
    
    /**
     * Update club location and stadium
     * @param clubId Club ID
     * @param userId User ID (must be owner or manager)
     * @param countyId New county ID
     * @param homeStadiumId Home stadium ID (optional)
     * @return Updated Club entity
     */
    Club updateClubLocation(Long clubId, Long userId, Long countyId, Long homeStadiumId);
    
    /**
     * Update club branding (colors, website, social media)
     * @param clubId Club ID
     * @param userId User ID (must be owner or manager)
     * @param colors Club colors
     * @param websiteUrl Website URL
     * @param socialMediaLinks Social media links (JSON)
     * @return Updated Club entity
     */
    Club updateClubBranding(Long clubId, Long userId, String colors, String websiteUrl, String socialMediaLinks);
    
    /**
     * Add FKF registration to grassroots club
     * @param clubId Club ID
     * @param userId User ID (must be owner)
     * @param registrationNumber FKF registration number
     * @param clubLevel New club level
     * @param founded Foundation date
     * @return Updated Club entity
     */
    Club addFkfRegistration(Long clubId, Long userId, String registrationNumber, 
                           Club.ClubLevel clubLevel, LocalDate founded);
    
    /**
     * Promote grassroots club to FKF status with comprehensive upgrades
     * @param clubId Club ID
     * @param userId User ID (must be owner)
     * @param fkfRegistrationNumber FKF registration number
     * @param fkfRegistrationDate FKF registration date
     * @param currentLeague Current league name
     * @param tier League tier
     * @param newLevel Optional new club level for promotion
     * @return Updated Club entity
     */
    Club promoteToFkf(Long clubId, Long userId, String fkfRegistrationNumber, 
                     LocalDate fkfRegistrationDate, String currentLeague, Integer tier, 
                     Club.ClubLevel newLevel);
    
    /**
     * Update club level (promotion/relegation)
     * @param clubId Club ID
     * @param newLevel New club level
     * @param userId Admin user ID
     * @return Updated Club entity
     */
    Club updateClubLevel(Long clubId, Club.ClubLevel newLevel, Long userId);
    
    // Verification Management
    
    /**
     * Submit club for LigiOpen verification
     * @param clubId Club ID
     * @param userId User ID (must be owner)
     * @return Updated Club entity
     */
    Club submitForLigiopenVerification(Long clubId, Long userId);
    
    /**
     * LigiOpen admin verify club
     * @param clubId Club ID
     * @param adminUserId Admin user ID
     * @param verified Verification status
     * @param notes Verification notes
     * @return Updated Club entity
     */
    Club ligiopenVerifyClub(Long clubId, Long adminUserId, boolean verified, String notes);
    
    /**
     * Update FKF verification status (admin only)
     * @param clubId Club ID
     * @param adminUserId Admin user ID
     * @param fkfStatus FKF verification status
     * @return Updated Club entity
     */
    Club updateFkfVerificationStatus(Long clubId, Long adminUserId, Club.FkfVerificationStatus fkfStatus);
    
    /**
     * Suspend club
     * @param clubId Club ID
     * @param adminUserId Admin user ID
     * @param reason Suspension reason
     * @return Updated Club entity
     */
    Club suspendClub(Long clubId, Long adminUserId, String reason);
    
    /**
     * Reactivate club
     * @param clubId Club ID
     * @param adminUserId Admin user ID
     * @return Updated Club entity
     */
    Club reactivateClub(Long clubId, Long adminUserId);
    
    // Ownership and Management
    
    /**
     * Transfer club ownership
     * @param clubId Club ID
     * @param currentOwnerId Current owner user ID
     * @param newOwnerId New owner user ID
     * @return Updated Club entity
     */
    Club transferOwnership(Long clubId, Long currentOwnerId, Long newOwnerId);
    
    /**
     * Add club manager
     * @param clubId Club ID
     * @param ownerId Owner user ID
     * @param managerId Manager user ID to add
     * @return Updated Club entity
     */
    Club addClubManager(Long clubId, Long ownerId, Long managerId);
    
    /**
     * Remove club manager
     * @param clubId Club ID
     * @param ownerId Owner user ID
     * @param managerId Manager user ID to remove
     * @return Updated Club entity
     */
    Club removeClubManager(Long clubId, Long ownerId, Long managerId);
    
    // Club Retrieval
    
    /**
     * Get club by ID
     * @param id Club ID
     * @return Club entity
     * @throws RuntimeException if club not found
     */
    Club getClubById(Long id);
    
    /**
     * Find club by ID (safe version)
     * @param id Club ID
     * @return Optional Club entity
     */
    Optional<Club> findClubById(Long id);
    
    /**
     * Get club by name
     * @param name Club name
     * @return Optional Club entity
     */
    Optional<Club> getClubByName(String name);
    
    /**
     * Get club by FKF registration number
     * @param registrationNumber FKF registration number
     * @return Optional Club entity
     */
    Optional<Club> getClubByRegistrationNumber(String registrationNumber);
    
    // Club Listings
    
    /**
     * Get all active clubs
     * @return List of active clubs
     */
    List<Club> getAllActiveClubs();
    
    /**
     * Get clubs by county
     * @param countyId County ID
     * @return List of clubs in the county
     */
    List<Club> getClubsByCounty(Long countyId);
    
    /**
     * Get clubs by region
     * @param region Region name
     * @return List of clubs in the region
     */
    List<Club> getClubsByRegion(String region);
    
    /**
     * Get clubs by level
     * @param clubLevel Club level
     * @return List of clubs at the level
     */
    List<Club> getClubsByLevel(Club.ClubLevel clubLevel);
    
    /**
     * Get clubs owned by user
     * @param userId User ID
     * @return List of clubs owned by user
     */
    List<Club> getClubsOwnedByUser(Long userId);
    
    /**
     * Get clubs managed by user
     * @param userId User ID
     * @return List of clubs managed by user
     */
    List<Club> getClubsManagedByUser(Long userId);
    
    /**
     * Get clubs favorited by user
     * @param userId User ID
     * @return List of clubs favorited by user
     */
    List<Club> getClubsFavoritedByUser(Long userId);
    
    // Verification Status Queries
    
    /**
     * Get clubs pending LigiOpen verification
     * @return List of clubs awaiting verification
     */
    List<Club> getClubsPendingVerification();
    
    /**
     * Get LigiOpen verified clubs
     * @return List of verified clubs
     */
    List<Club> getLigiopenVerifiedClubs();
    
    /**
     * Get FKF verified clubs
     * @return List of FKF verified clubs
     */
    List<Club> getFkfVerifiedClubs();
    
    /**
     * Get fully verified clubs (both LigiOpen and FKF)
     * @return List of fully verified clubs
     */
    List<Club> getFullyVerifiedClubs();
    
    /**
     * Get grassroots clubs
     * @return List of grassroots clubs
     */
    List<Club> getGrassrootsClubs();
    
    /**
     * Get recently created clubs
     * @param limit Number of clubs to return
     * @return List of recently created clubs
     */
    List<Club> getRecentlyCreatedClubs(int limit);
    
    // Search and Discovery
    
    /**
     * Search clubs by name
     * @param searchTerm Search term
     * @return List of clubs matching search
     */
    List<Club> searchClubsByName(String searchTerm);
    
    // Permission Checks
    
    /**
     * Check if user can manage club
     * @param userId User ID
     * @param clubId Club ID
     * @return true if user is owner or manager
     */
    boolean canUserManageClub(Long userId, Long clubId);
    
    /**
     * Check if user owns club
     * @param userId User ID
     * @param clubId Club ID
     * @return true if user is owner
     */
    boolean isUserClubOwner(Long userId, Long clubId);
    
    /**
     * Check if user manages club
     * @param userId User ID
     * @param clubId Club ID
     * @return true if user is manager
     */
    boolean isUserClubManager(Long userId, Long clubId);
    
    // Statistics
    
    /**
     * Get total club count
     * @return Total number of clubs
     */
    long getTotalClubCount();
    
    /**
     * Get active club count
     * @return Number of active clubs
     */
    long getActiveClubCount();
    
    /**
     * Get verified club count
     * @param verificationType "ligiopen", "fkf", or "both"
     * @return Number of verified clubs
     */
    long getVerifiedClubCount(String verificationType);
    
    /**
     * Get club count by level
     * @param level Club level
     * @return Number of clubs at the level
     */
    long getClubCountByLevel(Club.ClubLevel level);
    
    /**
     * Delete club (soft delete - sets inactive)
     * @param clubId Club ID
     * @param userId User ID (must be owner)
     * @throws RuntimeException if user not authorized or club has dependencies
     */
    void deleteClub(Long clubId, Long userId);
    
    // Extended CRUD operations for Club Profile Management
    
    /**
     * Update comprehensive club information
     * @param clubId Club ID
     * @param userId User ID (must be owner or manager)
     * @param name Club name
     * @param shortName Club short name
     * @param abbreviation Club abbreviation
     * @param founded Foundation date
     * @param description Club description
     * @param colors Club colors
     * @param contactEmail Contact email
     * @param contactPhone Contact phone
     * @param websiteUrl Website URL
     * @param socialMediaLinks Social media links
     * @param countyId County ID
     * @param homeStadiumId Home stadium ID
     * @param clubLevel Club level
     * @return Updated Club entity
     */
    Club updateClub(Long clubId, Long userId, String name, String shortName, String abbreviation,
                   LocalDate founded, String description, String colors, String contactEmail,
                   String contactPhone, String websiteUrl, String socialMediaLinks,
                   Long countyId, Long homeStadiumId, Club.ClubLevel clubLevel);
    
    /**
     * Update club logo
     * @param clubId Club ID
     * @param userId User ID (must be owner or manager)
     * @param logoFile Logo file
     * @return Updated Club entity
     */
    Club updateClubLogo(Long clubId, Long userId, MultipartFile logoFile);
    
    /**
     * Update club main photo
     * @param clubId Club ID
     * @param userId User ID (must be owner or manager)
     * @param photoFile Photo file
     * @return Updated Club entity
     */
    Club updateClubMainPhoto(Long clubId, Long userId, MultipartFile photoFile);
    
    /**
     * Delete club logo
     * @param clubId Club ID
     * @param userId User ID (must be owner or manager)
     * @return Updated Club entity
     */
    Club deleteClubLogo(Long clubId, Long userId);
    
    /**
     * Delete club main photo
     * @param clubId Club ID
     * @param userId User ID (must be owner or manager)
     * @return Updated Club entity
     */
    Club deleteClubMainPhoto(Long clubId, Long userId);
    
    // Paginated search and filtering methods
    
    /**
     * Search clubs by name with pagination
     * @param searchTerm Search term
     * @param pageRequest Pagination info
     * @return List of clubs matching search
     */
    List<Club> searchClubsByName(String searchTerm, PageRequest pageRequest);
    
    /**
     * Get clubs by county with pagination
     * @param countyId County ID
     * @param pageRequest Pagination info
     * @return List of clubs in the county
     */
    List<Club> getClubsByCounty(Long countyId, PageRequest pageRequest);
    
    /**
     * Get clubs by region with pagination
     * @param region Region name
     * @param pageRequest Pagination info
     * @return List of clubs in the region
     */
    List<Club> getClubsByRegion(String region, PageRequest pageRequest);
    
    /**
     * Get clubs by level with pagination
     * @param clubLevel Club level
     * @param pageRequest Pagination info
     * @return List of clubs at the level
     */
    List<Club> getClubsByLevel(Club.ClubLevel clubLevel, PageRequest pageRequest);
    
    /**
     * Get clubs near location (geographic search)
     * @param latitude Latitude
     * @param longitude Longitude
     * @param radiusKm Search radius in kilometers
     * @param pageRequest Pagination info
     * @return List of clubs near location
     */
    List<Club> getClubsNearLocation(BigDecimal latitude, BigDecimal longitude, 
                                   double radiusKm, PageRequest pageRequest);
    
    /**
     * Get verified clubs with pagination
     * @param verificationType "ligiopen", "fkf", or "both"
     * @param pageRequest Pagination info
     * @return List of verified clubs
     */
    List<Club> getVerifiedClubs(String verificationType, PageRequest pageRequest);
    
    /**
     * Get all active clubs with pagination
     * @param pageRequest Pagination info
     * @return List of active clubs
     */
    List<Club> getAllActiveClubs(PageRequest pageRequest);
    
    /**
     * Get club by registration number (returns entity directly, throws if not found)
     * @param registrationNumber FKF registration number
     * @return Club entity
     * @throws RuntimeException if club not found
     */
    Club getClubByRegistrationNumberOrThrow(String registrationNumber);
    
    /**
     * Get club statistics
     * @param clubId Club ID
     * @return Map containing club statistics
     */
    Map<String, Object> getClubStatistics(Long clubId);
}