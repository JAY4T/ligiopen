package com.jabulani.ligiopen.service.club;

import com.jabulani.ligiopen.dao.ClubDao;
import com.jabulani.ligiopen.dao.CountyDao;
import com.jabulani.ligiopen.dao.StadiumDao;
import com.jabulani.ligiopen.dao.UserEntityDao;
import com.jabulani.ligiopen.entity.club.Club;
import com.jabulani.ligiopen.entity.location.County;
import com.jabulani.ligiopen.entity.location.Stadium;
import com.jabulani.ligiopen.entity.user.UserEntity;
import com.jabulani.ligiopen.service.file.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ClubServiceImpl implements ClubService {

    private static final Logger logger = LoggerFactory.getLogger(ClubServiceImpl.class);

    private final ClubDao clubDao;
    private final CountyDao countyDao;
    private final StadiumDao stadiumDao;
    private final UserEntityDao userEntityDao;
    private final FileUploadService fileUploadService;

    @Autowired
    public ClubServiceImpl(ClubDao clubDao, CountyDao countyDao, 
                          StadiumDao stadiumDao, UserEntityDao userEntityDao,
                          FileUploadService fileUploadService) {
        this.clubDao = clubDao;
        this.countyDao = countyDao;
        this.stadiumDao = stadiumDao;
        this.userEntityDao = userEntityDao;
        this.fileUploadService = fileUploadService;
    }

    @Override
    @Transactional
    public Club registerClub(String name, String shortName, Long ownerId, Long countyId, 
                           String description, String contactEmail, String contactPhone) {
        logger.info("Registering grassroots club: {} by owner ID: {}", name, ownerId);
        
        validateClubRegistrationInput(name, ownerId, countyId, contactEmail);
        
        UserEntity owner = userEntityDao.getUserById(ownerId);
        County county = countyDao.getCountyById(countyId);
        
        // Check for duplicate club name in the same county
        if (clubDao.existsByNameAndCounty(name.trim(), county)) {
            throw new RuntimeException("Club with name '" + name.trim() + "' already exists in " + county.getName());
        }
        
        Club club = Club.builder()
                .name(name.trim())
                .shortName(shortName != null ? shortName.trim() : null)
                .owner(owner)
                .county(county)
                .description(description != null ? description.trim() : null)
                .contactEmail(contactEmail.trim())
                .contactPhone(contactPhone != null ? contactPhone.trim() : null)
                .clubLevel(Club.ClubLevel.GRASSROOTS)
                .isLigiopenVerified(false)
                .ligiopenVerificationStatus(Club.LigiopenVerificationStatus.PENDING)
                .isFkfVerified(false)
                .fkfVerificationStatus(Club.FkfVerificationStatus.NOT_APPLICABLE)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        return clubDao.createClub(club);
    }

    @Override
    @Transactional
    public Club registerClubWithFkfNumber(String name, String shortName, Long ownerId, Long countyId,
                                        String description, String contactEmail, String contactPhone,
                                        String registrationNumber, Club.ClubLevel clubLevel, LocalDate founded) {
        logger.info("Registering club with FKF number: {} ({})", name, registrationNumber);
        
        validateClubRegistrationInput(name, ownerId, countyId, contactEmail);
        validateFkfRegistrationInput(registrationNumber, clubLevel);
        
        UserEntity owner = userEntityDao.getUserById(ownerId);
        County county = countyDao.getCountyById(countyId);
        
        // Check for duplicate club name in the same county
        if (clubDao.existsByNameAndCounty(name.trim(), county)) {
            throw new RuntimeException("Club with name '" + name.trim() + "' already exists in " + county.getName());
        }
        
        // Check for duplicate FKF registration number
        if (clubDao.existsByRegistrationNumber(registrationNumber.trim())) {
            throw new RuntimeException("Club with FKF registration number '" + registrationNumber.trim() + "' already exists");
        }
        
        Club club = Club.builder()
                .name(name.trim())
                .shortName(shortName != null ? shortName.trim() : null)
                .owner(owner)
                .county(county)
                .description(description != null ? description.trim() : null)
                .contactEmail(contactEmail.trim())
                .contactPhone(contactPhone != null ? contactPhone.trim() : null)
                .registrationNumber(registrationNumber.trim())
                .clubLevel(clubLevel)
                .founded(founded)
                .isLigiopenVerified(false)
                .ligiopenVerificationStatus(Club.LigiopenVerificationStatus.PENDING)
                .isFkfVerified(false)
                .fkfVerificationStatus(Club.FkfVerificationStatus.PENDING)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        return clubDao.createClub(club);
    }

    @Override
    @Transactional
    public Club updateClubInfo(Long clubId, Long userId, String name, String shortName, 
                             String description, String contactEmail, String contactPhone) {
        logger.info("Updating club info for club ID: {} by user ID: {}", clubId, userId);
        
        validateClubManagementPermission(clubId, userId);
        validateBasicClubInfo(name, contactEmail);
        
        Club club = clubDao.getClubById(clubId);
        
        // Check for duplicate club name in the same county (excluding current club)
        if (name != null && !name.equals(club.getName()) && 
            clubDao.existsByNameAndCounty(name.trim(), club.getCounty())) {
            throw new RuntimeException("Club with name '" + name.trim() + "' already exists in " + club.getCounty().getName());
        }
        
        if (name != null) club.setName(name.trim());
        if (shortName != null) club.setShortName(shortName.trim().isEmpty() ? null : shortName.trim());
        if (description != null) club.setDescription(description.trim().isEmpty() ? null : description.trim());
        if (contactEmail != null) club.setContactEmail(contactEmail.trim());
        if (contactPhone != null) club.setContactPhone(contactPhone.trim().isEmpty() ? null : contactPhone.trim());
        
        club.setUpdatedAt(LocalDateTime.now());
        
        return clubDao.updateClub(club);
    }

    @Override
    @Transactional
    public Club updateClubLocation(Long clubId, Long userId, Long countyId, Long homeStadiumId) {
        logger.info("Updating location for club ID: {} to county ID: {}", clubId, countyId);
        
        validateClubManagementPermission(clubId, userId);
        
        Club club = clubDao.getClubById(clubId);
        County newCounty = countyDao.getCountyById(countyId);
        
        // Check if club name conflicts in new county
        if (!club.getCounty().getId().equals(countyId) && 
            clubDao.existsByNameAndCounty(club.getName(), newCounty)) {
            throw new RuntimeException("Club with name '" + club.getName() + "' already exists in " + newCounty.getName());
        }
        
        Stadium homeStadium = null;
        if (homeStadiumId != null) {
            homeStadium = stadiumDao.getStadiumById(homeStadiumId);
            // Verify stadium is in the same county
            if (!homeStadium.getCounty().getId().equals(countyId)) {
                throw new RuntimeException("Stadium must be in the same county as the club");
            }
        }
        
        club.setCounty(newCounty);
        club.setHomeStadium(homeStadium);
        club.setUpdatedAt(LocalDateTime.now());
        
        return clubDao.updateClub(club);
    }

    @Override
    @Transactional
    public Club updateClubBranding(Long clubId, Long userId, String colors, String websiteUrl, String socialMediaLinks) {
        logger.info("Updating branding for club ID: {}", clubId);
        
        validateClubManagementPermission(clubId, userId);
        
        Club club = clubDao.getClubById(clubId);
        
        if (colors != null) club.setColors(colors.trim().isEmpty() ? null : colors.trim());
        if (websiteUrl != null) {
            String url = websiteUrl.trim();
            if (!url.isEmpty() && !url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
            }
            club.setWebsiteUrl(url.isEmpty() ? null : url);
        }
        if (socialMediaLinks != null) club.setSocialMediaLinks(socialMediaLinks.trim().isEmpty() ? null : socialMediaLinks.trim());
        
        club.setUpdatedAt(LocalDateTime.now());
        
        return clubDao.updateClub(club);
    }

    @Override
    @Transactional
    public Club addFkfRegistration(Long clubId, Long userId, String registrationNumber, 
                                 Club.ClubLevel clubLevel, LocalDate founded) {
        logger.info("Adding FKF registration to club ID: {} with number: {}", clubId, registrationNumber);
        
        validateClubOwnershipPermission(clubId, userId);
        validateFkfRegistrationInput(registrationNumber, clubLevel);
        
        Club club = clubDao.getClubById(clubId);
        
        if (club.getRegistrationNumber() != null && !club.getRegistrationNumber().isEmpty()) {
            throw new RuntimeException("Club already has FKF registration number: " + club.getRegistrationNumber());
        }
        
        // Check for duplicate FKF registration number
        if (clubDao.existsByRegistrationNumber(registrationNumber.trim())) {
            throw new RuntimeException("FKF registration number '" + registrationNumber.trim() + "' already exists");
        }
        
        club.setRegistrationNumber(registrationNumber.trim());
        club.setClubLevel(clubLevel);
        club.setFounded(founded);
        club.setFkfVerificationStatus(Club.FkfVerificationStatus.PENDING);
        club.setFkfRegistrationDate(LocalDate.now());
        club.setUpdatedAt(LocalDateTime.now());
        
        return clubDao.updateClub(club);
    }

    @Override
    @Transactional
    public Club updateClubLevel(Long clubId, Club.ClubLevel newLevel, Long userId) {
        logger.info("Updating club level for club ID: {} to {}", clubId, newLevel);
        
        // TODO: Add admin permission check when UserRole enum is available
        
        Club club = clubDao.getClubById(clubId);
        club.setClubLevel(newLevel);
        club.setUpdatedAt(LocalDateTime.now());
        
        return clubDao.updateClub(club);
    }

    @Override
    @Transactional
    public Club submitForLigiopenVerification(Long clubId, Long userId) {
        logger.info("Submitting club ID: {} for LigiOpen verification", clubId);
        
        validateClubOwnershipPermission(clubId, userId);
        
        Club club = clubDao.getClubById(clubId);
        
        if (club.getLigiopenVerificationStatus() == Club.LigiopenVerificationStatus.VERIFIED) {
            throw new RuntimeException("Club is already verified by LigiOpen");
        }
        
        if (club.getLigiopenVerificationStatus() == Club.LigiopenVerificationStatus.UNDER_REVIEW) {
            throw new RuntimeException("Club is already under review by LigiOpen");
        }
        
        club.setLigiopenVerificationStatus(Club.LigiopenVerificationStatus.UNDER_REVIEW);
        club.setUpdatedAt(LocalDateTime.now());
        
        return clubDao.updateClub(club);
    }

    @Override
    @Transactional
    public Club ligiopenVerifyClub(Long clubId, Long adminUserId, boolean verified, String notes) {
        logger.info("LigiOpen {} club ID: {} by admin ID: {}", verified ? "verifying" : "rejecting", clubId, adminUserId);
        
        // TODO: Add admin permission check when UserRole enum is available
        
        Club club = clubDao.getClubById(clubId);
        
        club.setIsLigiopenVerified(verified);
        club.setLigiopenVerificationStatus(verified ? 
            Club.LigiopenVerificationStatus.VERIFIED : 
            Club.LigiopenVerificationStatus.REJECTED);
        club.setLigiopenVerificationDate(LocalDateTime.now());
        club.setLigiopenVerificationNotes(notes != null ? notes.trim() : null);
        club.setUpdatedAt(LocalDateTime.now());
        
        return clubDao.updateClub(club);
    }

    @Override
    @Transactional
    public Club updateFkfVerificationStatus(Long clubId, Long adminUserId, Club.FkfVerificationStatus fkfStatus) {
        logger.info("Updating FKF verification status for club ID: {} to {}", clubId, fkfStatus);
        
        // TODO: Add admin permission check when UserRole enum is available
        
        Club club = clubDao.getClubById(clubId);
        
        if (fkfStatus == Club.FkfVerificationStatus.NOT_APPLICABLE && 
            (club.getRegistrationNumber() != null && !club.getRegistrationNumber().isEmpty())) {
            throw new RuntimeException("Cannot set FKF status to NOT_APPLICABLE for club with registration number");
        }
        
        club.setIsFkfVerified(fkfStatus == Club.FkfVerificationStatus.VERIFIED);
        club.setFkfVerificationStatus(fkfStatus);
        club.setFkfVerificationDate(fkfStatus == Club.FkfVerificationStatus.VERIFIED ? LocalDateTime.now() : null);
        club.setUpdatedAt(LocalDateTime.now());
        
        return clubDao.updateClub(club);
    }

    @Override
    @Transactional
    public Club suspendClub(Long clubId, Long adminUserId, String reason) {
        logger.info("Suspending club ID: {} by admin ID: {}", clubId, adminUserId);
        
        // TODO: Add admin permission check when UserRole enum is available
        
        Club club = clubDao.getClubById(clubId);
        
        club.setLigiopenVerificationStatus(Club.LigiopenVerificationStatus.SUSPENDED);
        club.setIsLigiopenVerified(false);
        club.setLigiopenVerificationNotes(reason != null ? reason.trim() : "Suspended by LigiOpen admin");
        club.setLigiopenVerificationDate(LocalDateTime.now());
        club.setIsActive(false);
        club.setUpdatedAt(LocalDateTime.now());
        
        return clubDao.updateClub(club);
    }

    @Override
    @Transactional
    public Club reactivateClub(Long clubId, Long adminUserId) {
        logger.info("Reactivating club ID: {} by admin ID: {}", clubId, adminUserId);
        
        // TODO: Add admin permission check when UserRole enum is available
        
        Club club = clubDao.getClubById(clubId);
        
        club.setLigiopenVerificationStatus(Club.LigiopenVerificationStatus.PENDING);
        club.setIsActive(true);
        club.setUpdatedAt(LocalDateTime.now());
        
        return clubDao.updateClub(club);
    }

    @Override
    @Transactional
    public Club transferOwnership(Long clubId, Long currentOwnerId, Long newOwnerId) {
        logger.info("Transferring ownership of club ID: {} from user {} to user {}", clubId, currentOwnerId, newOwnerId);
        
        validateClubOwnershipPermission(clubId, currentOwnerId);
        
        Club club = clubDao.getClubById(clubId);
        UserEntity newOwner = userEntityDao.getUserById(newOwnerId);
        
        club.setOwner(newOwner);
        club.setUpdatedAt(LocalDateTime.now());
        
        return clubDao.updateClub(club);
    }

    @Override
    @Transactional
    public Club addClubManager(Long clubId, Long ownerId, Long managerId) {
        logger.info("Adding manager {} to club ID: {}", managerId, clubId);
        
        validateClubOwnershipPermission(clubId, ownerId);
        
        Club club = clubDao.getClubById(clubId);
        UserEntity manager = userEntityDao.getUserById(managerId);
        
        if (club.getManagers().contains(manager)) {
            throw new RuntimeException("User is already a manager of this club");
        }
        
        if (club.getOwner().getId().equals(managerId)) {
            throw new RuntimeException("Club owner cannot be added as manager");
        }
        
        club.getManagers().add(manager);
        club.setUpdatedAt(LocalDateTime.now());
        
        return clubDao.updateClub(club);
    }

    @Override
    @Transactional
    public Club removeClubManager(Long clubId, Long ownerId, Long managerId) {
        logger.info("Removing manager {} from club ID: {}", managerId, clubId);
        
        validateClubOwnershipPermission(clubId, ownerId);
        
        Club club = clubDao.getClubById(clubId);
        UserEntity manager = userEntityDao.getUserById(managerId);
        
        if (!club.getManagers().contains(manager)) {
            throw new RuntimeException("User is not a manager of this club");
        }
        
        club.getManagers().remove(manager);
        club.setUpdatedAt(LocalDateTime.now());
        
        return clubDao.updateClub(club);
    }

    // Club Retrieval Methods

    @Override
    public Club getClubById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Club ID is required");
        }
        return clubDao.getClubById(id);
    }

    @Override
    public Optional<Club> findClubById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return clubDao.findClubById(id);
    }

    @Override
    public Optional<Club> getClubByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return Optional.empty();
        }
        return clubDao.getClubByName(name.trim());
    }

    @Override
    public Optional<Club> getClubByRegistrationNumber(String registrationNumber) {
        if (registrationNumber == null || registrationNumber.trim().isEmpty()) {
            return Optional.empty();
        }
        return clubDao.getClubByRegistrationNumber(registrationNumber.trim());
    }

    // Club Listings

    @Override
    public List<Club> getAllActiveClubs() {
        logger.debug("Fetching all active clubs");
        return clubDao.getActiveClubs();
    }

    @Override
    public List<Club> getClubsByCounty(Long countyId) {
        if (countyId == null) {
            throw new IllegalArgumentException("County ID is required");
        }
        logger.debug("Fetching clubs for county ID: {}", countyId);
        return clubDao.getClubsByCountyId(countyId);
    }

    @Override
    public List<Club> getClubsByRegion(String region) {
        if (region == null || region.trim().isEmpty()) {
            throw new IllegalArgumentException("Region is required");
        }
        logger.debug("Fetching clubs for region: {}", region);
        return clubDao.getClubsByRegion(region.trim());
    }

    @Override
    public List<Club> getClubsByLevel(Club.ClubLevel clubLevel) {
        if (clubLevel == null) {
            throw new IllegalArgumentException("Club level is required");
        }
        logger.debug("Fetching clubs for level: {}", clubLevel);
        return clubDao.getClubsByLevel(clubLevel);
    }

    @Override
    public List<Club> getClubsOwnedByUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required");
        }
        logger.debug("Fetching clubs owned by user ID: {}", userId);
        return clubDao.getClubsByOwnerId(userId);
    }

    @Override
    public List<Club> getClubsManagedByUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required");
        }
        logger.debug("Fetching clubs managed by user ID: {}", userId);
        return clubDao.getClubsManagedByUserId(userId);
    }

    @Override
    public List<Club> getClubsFavoritedByUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required");
        }
        logger.debug("Fetching clubs favorited by user ID: {}", userId);
        return clubDao.getClubsFavoritedByUserId(userId);
    }

    // Verification Status Queries

    @Override
    public List<Club> getClubsPendingVerification() {
        logger.debug("Fetching clubs pending LigiOpen verification");
        return clubDao.getClubsPendingLigiopenVerification();
    }

    @Override
    public List<Club> getLigiopenVerifiedClubs() {
        logger.debug("Fetching LigiOpen verified clubs");
        return clubDao.getLigiopenVerifiedClubs();
    }

    @Override
    public List<Club> getFkfVerifiedClubs() {
        logger.debug("Fetching FKF verified clubs");
        return clubDao.getFkfVerifiedClubs();
    }

    @Override
    public List<Club> getFullyVerifiedClubs() {
        logger.debug("Fetching fully verified clubs");
        return clubDao.getFullyVerifiedClubs();
    }

    @Override
    public List<Club> getGrassrootsClubs() {
        logger.debug("Fetching grassroots clubs");
        return clubDao.getGrassrootsClubs();
    }

    @Override
    public List<Club> getRecentlyCreatedClubs(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be positive");
        }
        if (limit > 100) {
            throw new IllegalArgumentException("Limit cannot exceed 100");
        }
        logger.debug("Fetching {} recently created clubs", limit);
        return clubDao.getRecentlyCreatedClubs(limit);
    }

    @Override
    public List<Club> searchClubsByName(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllActiveClubs();
        }
        logger.debug("Searching clubs with term: {}", searchTerm);
        return clubDao.searchClubsByName(searchTerm.trim());
    }

    // Permission Checks

    @Override
    public boolean canUserManageClub(Long userId, Long clubId) {
        if (userId == null || clubId == null) {
            return false;
        }
        return isUserClubOwner(userId, clubId) || isUserClubManager(userId, clubId);
    }

    @Override
    public boolean isUserClubOwner(Long userId, Long clubId) {
        if (userId == null || clubId == null) {
            return false;
        }
        return clubDao.isUserClubOwner(userId, clubId);
    }

    @Override
    public boolean isUserClubManager(Long userId, Long clubId) {
        if (userId == null || clubId == null) {
            return false;
        }
        return clubDao.isUserClubManager(userId, clubId);
    }

    // Statistics

    @Override
    public long getTotalClubCount() {
        return clubDao.countClubs();
    }

    @Override
    public long getActiveClubCount() {
        return clubDao.countActiveClubs();
    }

    @Override
    public long getVerifiedClubCount(String verificationType) {
        if (verificationType == null) {
            throw new IllegalArgumentException("Verification type is required");
        }
        
        switch (verificationType.toLowerCase()) {
            case "ligiopen":
                return clubDao.countLigiopenVerifiedClubs();
            case "fkf":
                return clubDao.countFkfVerifiedClubs();
            case "both":
                // This would require a custom query - for now approximate
                return Math.min(clubDao.countLigiopenVerifiedClubs(), clubDao.countFkfVerifiedClubs());
            default:
                throw new IllegalArgumentException("Invalid verification type: " + verificationType + 
                    ". Must be 'ligiopen', 'fkf', or 'both'");
        }
    }

    @Override
    public long getClubCountByLevel(Club.ClubLevel level) {
        if (level == null) {
            throw new IllegalArgumentException("Club level is required");
        }
        return clubDao.countClubsByLevel(level);
    }

    @Override
    @Transactional
    public void deleteClub(Long clubId, Long userId) {
        logger.info("Deleting club ID: {} by user ID: {}", clubId, userId);
        
        validateClubOwnershipPermission(clubId, userId);
        
        // Soft delete - set inactive instead of hard delete
        Club club = clubDao.getClubById(clubId);
        club.setIsActive(false);
        club.setUpdatedAt(LocalDateTime.now());
        
        clubDao.updateClub(club);
        logger.info("Club {} marked as inactive", club.getName());
    }

    // Private helper methods

    private void validateClubRegistrationInput(String name, Long ownerId, Long countyId, String contactEmail) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Club name is required");
        }
        if (name.trim().length() < 2) {
            throw new IllegalArgumentException("Club name must be at least 2 characters long");
        }
        if (name.trim().length() > 100) {
            throw new IllegalArgumentException("Club name cannot exceed 100 characters");
        }
        if (ownerId == null) {
            throw new IllegalArgumentException("Owner ID is required");
        }
        if (countyId == null) {
            throw new IllegalArgumentException("County ID is required");
        }
        if (contactEmail == null || contactEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("Contact email is required");
        }
        if (!isValidEmail(contactEmail.trim())) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    private void validateFkfRegistrationInput(String registrationNumber, Club.ClubLevel clubLevel) {
        if (registrationNumber == null || registrationNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("FKF registration number is required");
        }
        if (registrationNumber.trim().length() < 3) {
            throw new IllegalArgumentException("FKF registration number must be at least 3 characters long");
        }
        if (clubLevel == null) {
            throw new IllegalArgumentException("Club level is required for FKF registered clubs");
        }
        if (clubLevel == Club.ClubLevel.GRASSROOTS) {
            throw new IllegalArgumentException("Grassroots clubs should not have FKF registration numbers");
        }
    }

    private void validateBasicClubInfo(String name, String contactEmail) {
        if (name != null && name.trim().length() > 0 && name.trim().length() < 2) {
            throw new IllegalArgumentException("Club name must be at least 2 characters long");
        }
        if (name != null && name.trim().length() > 100) {
            throw new IllegalArgumentException("Club name cannot exceed 100 characters");
        }
        if (contactEmail != null && !contactEmail.trim().isEmpty() && !isValidEmail(contactEmail.trim())) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    private void validateClubOwnershipPermission(Long clubId, Long userId) {
        if (!isUserClubOwner(userId, clubId)) {
            throw new RuntimeException("User does not have permission to perform this action. Must be club owner.");
        }
    }

    private void validateClubManagementPermission(Long clubId, Long userId) {
        if (!canUserManageClub(userId, clubId)) {
            throw new RuntimeException("User does not have permission to perform this action. Must be club owner or manager.");
        }
    }

    private boolean isValidEmail(String email) {
        // Basic email validation - in production, use a proper email validation library
        return email != null && email.contains("@") && email.contains(".") && email.length() > 5;
    }

    // Extended CRUD operations for Club Profile Management

    @Override
    @Transactional
    public Club updateClub(Long clubId, Long userId, String name, String shortName, String abbreviation,
                          LocalDate founded, String description, String colors, String contactEmail,
                          String contactPhone, String websiteUrl, String socialMediaLinks,
                          Long countyId, Long homeStadiumId, Club.ClubLevel clubLevel) {
        logger.info("Updating comprehensive club info for club ID: {} by user ID: {}", clubId, userId);
        
        validateClubManagementPermission(clubId, userId);
        
        Club club = getClubById(clubId);
        
        // Update basic information if provided
        if (name != null && !name.trim().isEmpty()) {
            validateBasicClubInfo(name, null);
            if (clubDao.existsByNameAndCountyAndIdNot(name.trim(), club.getCounty().getId(), clubId)) {
                throw new RuntimeException("A club with this name already exists in the county");
            }
            club.setName(name.trim());
        }
        
        if (shortName != null && !shortName.trim().isEmpty()) {
            club.setShortName(shortName.trim());
        }
        
        if (abbreviation != null && !abbreviation.trim().isEmpty()) {
            club.setAbbreviation(abbreviation.trim().toUpperCase());
        }
        
        if (founded != null) {
            club.setFounded(founded);
        }
        
        if (description != null) {
            club.setDescription(description.trim().isEmpty() ? null : description.trim());
        }
        
        if (colors != null) {
            club.setColors(colors.trim().isEmpty() ? null : colors.trim());
        }
        
        if (contactEmail != null && !contactEmail.trim().isEmpty()) {
            validateBasicClubInfo(null, contactEmail);
            club.setContactEmail(contactEmail.trim());
        }
        
        if (contactPhone != null) {
            club.setContactPhone(contactPhone.trim().isEmpty() ? null : contactPhone.trim());
        }
        
        if (websiteUrl != null) {
            club.setWebsiteUrl(websiteUrl.trim().isEmpty() ? null : websiteUrl.trim());
        }
        
        if (socialMediaLinks != null) {
            club.setSocialMediaLinks(socialMediaLinks.trim().isEmpty() ? null : socialMediaLinks.trim());
        }
        
        // Update location if provided
        if (countyId != null) {
            County county = countyDao.getCountyById(countyId);
            club.setCounty(county);
        }
        
        if (homeStadiumId != null) {
            Stadium stadium = stadiumDao.getStadiumById(homeStadiumId);
            club.setHomeStadium(stadium);
        }
        
        // Update club level if provided (only owner can change this)
        if (clubLevel != null && isUserClubOwner(userId, clubId)) {
            club.setClubLevel(clubLevel);
        }
        
        club.setUpdatedAt(LocalDateTime.now());
        
        return clubDao.updateClub(club);
    }

    @Override
    @Transactional
    public Club updateClubLogo(Long clubId, Long userId, MultipartFile logoFile) {
        logger.info("Updating club logo for club ID: {} by user ID: {}", clubId, userId);
        
        validateClubManagementPermission(clubId, userId);
        
        Club club = getClubById(clubId);
        
        try {
            // Delete existing logo if present
            if (club.getLogoFileId() != null) {
                fileUploadService.deleteFile(club.getLogoFileId().intValue());
            }
            
            // Upload new logo
            var uploadResult = fileUploadService.uploadFile(logoFile, userId, "Club logo", true);
            club.setLogoFileId(uploadResult.getId().longValue());
            club.setUpdatedAt(LocalDateTime.now());
            
            return clubDao.updateClub(club);
            
        } catch (Exception e) {
            logger.error("Failed to upload club logo for club ID: {}", clubId, e);
            throw new RuntimeException("Failed to upload club logo: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Club updateClubMainPhoto(Long clubId, Long userId, MultipartFile photoFile) {
        logger.info("Updating club main photo for club ID: {} by user ID: {}", clubId, userId);
        
        validateClubManagementPermission(clubId, userId);
        
        Club club = getClubById(clubId);
        
        try {
            // Delete existing photo if present
            if (club.getMainPhotoId() != null) {
                fileUploadService.deleteFile(club.getMainPhotoId().intValue());
            }
            
            // Upload new photo
            var uploadResult = fileUploadService.uploadFile(photoFile, userId, "Club main photo", true);
            club.setMainPhotoId(uploadResult.getId().longValue());
            club.setUpdatedAt(LocalDateTime.now());
            
            return clubDao.updateClub(club);
            
        } catch (Exception e) {
            logger.error("Failed to upload club main photo for club ID: {}", clubId, e);
            throw new RuntimeException("Failed to upload club main photo: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Club deleteClubLogo(Long clubId, Long userId) {
        logger.info("Deleting club logo for club ID: {} by user ID: {}", clubId, userId);
        
        validateClubManagementPermission(clubId, userId);
        
        Club club = getClubById(clubId);
        
        if (club.getLogoFileId() != null) {
            try {
                fileUploadService.deleteFile(club.getLogoFileId().intValue());
                club.setLogoFileId(null);
                club.setUpdatedAt(LocalDateTime.now());
                
                return clubDao.updateClub(club);
                
            } catch (Exception e) {
                logger.error("Failed to delete club logo for club ID: {}", clubId, e);
                throw new RuntimeException("Failed to delete club logo: " + e.getMessage());
            }
        }
        
        return club;
    }

    @Override
    @Transactional
    public Club deleteClubMainPhoto(Long clubId, Long userId) {
        logger.info("Deleting club main photo for club ID: {} by user ID: {}", clubId, userId);
        
        validateClubManagementPermission(clubId, userId);
        
        Club club = getClubById(clubId);
        
        if (club.getMainPhotoId() != null) {
            try {
                fileUploadService.deleteFile(club.getMainPhotoId().intValue());
                club.setMainPhotoId(null);
                club.setUpdatedAt(LocalDateTime.now());
                
                return clubDao.updateClub(club);
                
            } catch (Exception e) {
                logger.error("Failed to delete club main photo for club ID: {}", clubId, e);
                throw new RuntimeException("Failed to delete club main photo: " + e.getMessage());
            }
        }
        
        return club;
    }

    // Paginated search and filtering methods

    @Override
    public List<Club> searchClubsByName(String searchTerm, PageRequest pageRequest) {
        logger.info("Searching clubs by name: '{}' with pagination", searchTerm);
        return clubDao.searchClubsByName(searchTerm, pageRequest.getOffset(), pageRequest.getPageSize());
    }

    @Override
    public List<Club> getClubsByCounty(Long countyId, PageRequest pageRequest) {
        logger.info("Getting clubs by county ID: {} with pagination", countyId);
        return clubDao.getClubsByCounty(countyId, pageRequest.getOffset(), pageRequest.getPageSize());
    }

    @Override
    public List<Club> getClubsByRegion(String region, PageRequest pageRequest) {
        logger.info("Getting clubs by region: {} with pagination", region);
        return clubDao.getClubsByRegion(region, pageRequest.getOffset(), pageRequest.getPageSize());
    }

    @Override
    public List<Club> getClubsByLevel(Club.ClubLevel clubLevel, PageRequest pageRequest) {
        logger.info("Getting clubs by level: {} with pagination", clubLevel);
        return clubDao.getClubsByLevel(clubLevel, pageRequest.getOffset(), pageRequest.getPageSize());
    }

    @Override
    public List<Club> getClubsNearLocation(BigDecimal latitude, BigDecimal longitude, 
                                          double radiusKm, PageRequest pageRequest) {
        logger.info("Getting clubs near location: {}, {} within {}km with pagination", 
                   latitude, longitude, radiusKm);
        return clubDao.getClubsNearLocation(latitude, longitude, radiusKm, 
                                           pageRequest.getOffset(), pageRequest.getPageSize());
    }

    @Override
    public List<Club> getVerifiedClubs(String verificationType, PageRequest pageRequest) {
        logger.info("Getting verified clubs of type: {} with pagination", verificationType);
        
        return switch (verificationType.toLowerCase()) {
            case "ligiopen" -> clubDao.getLigiopenVerifiedClubs(pageRequest.getOffset(), pageRequest.getPageSize());
            case "fkf" -> clubDao.getFkfVerifiedClubs(pageRequest.getOffset(), pageRequest.getPageSize());
            case "both" -> clubDao.getFullyVerifiedClubs(pageRequest.getOffset(), pageRequest.getPageSize());
            default -> throw new IllegalArgumentException("Invalid verification type: " + verificationType + 
                                                        ". Must be 'ligiopen', 'fkf', or 'both'");
        };
    }

    @Override
    public List<Club> getAllActiveClubs(PageRequest pageRequest) {
        logger.info("Getting all active clubs with pagination");
        return clubDao.getAllActiveClubs(pageRequest.getOffset(), pageRequest.getPageSize());
    }

    @Override
    public Club getClubByRegistrationNumberOrThrow(String registrationNumber) {
        logger.info("Getting club by registration number: {}", registrationNumber);
        
        Optional<Club> club = clubDao.getClubByRegistrationNumber(registrationNumber);
        if (club.isEmpty()) {
            throw new RuntimeException("Club not found with registration number: " + registrationNumber);
        }
        
        return club.get();
    }

    @Override
    public Map<String, Object> getClubStatistics(Long clubId) {
        logger.info("Getting statistics for club ID: {}", clubId);
        
        Club club = getClubById(clubId);
        
        Map<String, Object> statistics = new HashMap<>();
        
        // Basic club info
        statistics.put("clubId", club.getId());
        statistics.put("clubName", club.getName());
        statistics.put("clubLevel", club.getClubLevel());
        statistics.put("isActive", club.getIsActive());
        statistics.put("createdAt", club.getCreatedAt());
        
        // Verification status
        statistics.put("isLigiopenVerified", club.getIsLigiopenVerified());
        statistics.put("isFkfVerified", club.getIsFkfVerified());
        statistics.put("ligiopenVerificationStatus", club.getLigiopenVerificationStatus());
        statistics.put("fkfVerificationStatus", club.getFkfVerificationStatus());
        
        // Counts - these would require additional DAO queries in a real implementation
        statistics.put("managersCount", club.getManagers() != null ? club.getManagers().size() : 0);
        statistics.put("favoritesCount", club.getFavoritedByUsers() != null ? club.getFavoritedByUsers().size() : 0);
        
        // Additional statistics would be calculated based on relationships:
        // statistics.put("playersCount", clubDao.getPlayersCount(clubId));
        // statistics.put("matchesPlayed", clubDao.getMatchesPlayedCount(clubId));
        // statistics.put("wins", clubDao.getWinsCount(clubId));
        // statistics.put("draws", clubDao.getDrawsCount(clubId));
        // statistics.put("losses", clubDao.getLossesCount(clubId));
        
        return statistics;
    }
}