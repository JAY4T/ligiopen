package com.jabulani.ligiopen.service.player;

import com.jabulani.ligiopen.dao.PlayerDao;
import com.jabulani.ligiopen.entity.player.Player;
import com.jabulani.ligiopen.entity.player.ClubMembership;
import com.jabulani.ligiopen.entity.player.PlayerTransfer;
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
import java.time.Period;
import java.util.*;

@Service
public class PlayerServiceImpl implements PlayerService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);

    private final PlayerDao playerDao;
    private final FileUploadService fileUploadService;

    @Autowired
    public PlayerServiceImpl(PlayerDao playerDao, FileUploadService fileUploadService) {
        this.playerDao = playerDao;
        this.fileUploadService = fileUploadService;
    }

    @Override
    @Transactional
    public Player registerPlayer(String firstName, String lastName, LocalDate dateOfBirth, 
                               String nationality, Player.Position primaryPosition, 
                               String email, String phoneNumber) {
        logger.info("Registering player: {} {} ({})", firstName, lastName, email);
        
        validatePlayerRegistrationInput(firstName, lastName, dateOfBirth, nationality, 
                                      primaryPosition, email, phoneNumber);
        
        // Check for duplicate email
        if (playerDao.existsByEmail(email.trim().toLowerCase())) {
            throw new RuntimeException("Player with email '" + email.trim() + "' already exists");
        }
        
        Player player = Player.builder()
                .firstName(firstName.trim())
                .lastName(lastName.trim())
                .dateOfBirth(dateOfBirth)
                .nationality(nationality.trim())
                .primaryPosition(primaryPosition)
                .email(email.trim().toLowerCase())
                .phoneNumber(phoneNumber != null ? phoneNumber.trim() : null)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        Player savedPlayer = playerDao.createPlayer(player);
        logger.info("Successfully registered player with ID: {}", savedPlayer.getId());
        return savedPlayer;
    }

    @Override
    public Optional<Player> getPlayerById(Long playerId) {
        logger.debug("Fetching player by ID: {}", playerId);
        return playerDao.findPlayerById(playerId);
    }

    @Override
    public Player getPlayerByIdOrThrow(Long playerId) {
        logger.debug("Fetching player by ID (or throw): {}", playerId);
        return playerDao.getPlayerById(playerId);
    }

    @Override
    @Transactional
    public Player updatePlayer(Long playerId, String firstName, String lastName, String nickname,
                             LocalDate dateOfBirth, String nationality, String secondNationality,
                             Double heightCm, Double weightKg, Player.PreferredFoot preferredFoot,
                             Player.Position primaryPosition, List<Player.Position> secondaryPositions,
                             String bio, String phoneNumber, String email, String emergencyContactName,
                             String emergencyContactPhone, String emergencyContactRelationship,
                             String idNumber, String passportNumber, String fkfRegistrationNumber,
                             BigDecimal marketValue) {
        
        logger.info("Updating player with ID: {}", playerId);
        
        Player player = playerDao.getPlayerById(playerId);
        
        // Validate and update email if changed
        if (email != null && !email.equals(player.getEmail())) {
            if (playerDao.existsByEmail(email.trim().toLowerCase())) {
                throw new RuntimeException("Player with email '" + email.trim() + "' already exists");
            }
            player.setEmail(email.trim().toLowerCase());
        }
        
        // Validate and update FKF registration number if changed
        if (fkfRegistrationNumber != null && !fkfRegistrationNumber.equals(player.getFkfRegistrationNumber())) {
            if (playerDao.existsByFkfRegistrationNumber(fkfRegistrationNumber.trim())) {
                throw new RuntimeException("Player with FKF registration number '" + fkfRegistrationNumber.trim() + "' already exists");
            }
            player.setFkfRegistrationNumber(fkfRegistrationNumber.trim());
        }
        
        // Validate and update ID number if changed
        if (idNumber != null && !idNumber.equals(player.getIdNumber())) {
            if (playerDao.existsByIdNumber(idNumber.trim())) {
                throw new RuntimeException("Player with ID number '" + idNumber.trim() + "' already exists");
            }
            player.setIdNumber(idNumber.trim());
        }
        
        // Validate and update passport number if changed
        if (passportNumber != null && !passportNumber.equals(player.getPassportNumber())) {
            if (playerDao.existsByPassportNumber(passportNumber.trim())) {
                throw new RuntimeException("Player with passport number '" + passportNumber.trim() + "' already exists");
            }
            player.setPassportNumber(passportNumber.trim());
        }
        
        // Update basic information
        if (firstName != null) player.setFirstName(firstName.trim());
        if (lastName != null) player.setLastName(lastName.trim());
        if (nickname != null) player.setNickname(nickname.trim());
        if (dateOfBirth != null) player.setDateOfBirth(dateOfBirth);
        if (nationality != null) player.setNationality(nationality.trim());
        if (secondNationality != null) player.setSecondNationality(secondNationality.trim());
        
        // Update physical attributes
        if (heightCm != null) player.setHeightCm(heightCm);
        if (weightKg != null) player.setWeightKg(weightKg);
        if (preferredFoot != null) player.setPreferredFoot(preferredFoot);
        
        // Update positions
        if (primaryPosition != null) player.setPrimaryPosition(primaryPosition);
        if (secondaryPositions != null) player.setSecondaryPositions(secondaryPositions);
        
        // Update contact and personal information
        if (bio != null) player.setBio(bio.trim());
        if (phoneNumber != null) player.setPhoneNumber(phoneNumber.trim());
        if (emergencyContactName != null) player.setEmergencyContactName(emergencyContactName.trim());
        if (emergencyContactPhone != null) player.setEmergencyContactPhone(emergencyContactPhone.trim());
        if (emergencyContactRelationship != null) player.setEmergencyContactRelationship(emergencyContactRelationship.trim());
        
        // Update market value
        if (marketValue != null) player.setMarketValue(marketValue);
        
        Player updatedPlayer = playerDao.updatePlayer(player);
        logger.info("Successfully updated player: {} {}", updatedPlayer.getFirstName(), updatedPlayer.getLastName());
        return updatedPlayer;
    }

    @Override
    @Transactional
    public Player updatePlayerMainPhoto(Long playerId, MultipartFile photoFile) {
        logger.info("Updating main photo for player ID: {}", playerId);
        
        Player player = playerDao.getPlayerById(playerId);
        
        try {
            // Upload photo to file service
            var uploadedFile = fileUploadService.uploadFile(photoFile, 1L, "Player main photo", true);
            
            // Delete old photo if exists
            if (player.getMainPhotoId() != null) {
                try {
                    fileUploadService.deleteFile(player.getMainPhotoId().intValue());
                } catch (Exception e) {
                    logger.warn("Failed to delete old main photo for player ID: {}", playerId, e);
                }
            }
            
            player.setMainPhotoId(uploadedFile.getId().longValue());
            
            Player updatedPlayer = playerDao.updatePlayer(player);
            logger.info("Successfully updated main photo for player: {} {}", 
                       updatedPlayer.getFirstName(), updatedPlayer.getLastName());
            return updatedPlayer;
            
        } catch (Exception e) {
            logger.error("Failed to update main photo for player ID: {}", playerId, e);
            throw new RuntimeException("Failed to upload player photo", e);
        }
    }

    @Override
    @Transactional
    public Player addPlayerPhoto(Long playerId, MultipartFile photoFile) {
        logger.info("Adding photo for player ID: {}", playerId);
        
        Player player = playerDao.getPlayerById(playerId);
        
        try {
            // Upload photo to file service
            var uploadedFile = fileUploadService.uploadFile(photoFile, 1L, "Player main photo", true);
            
            // Add to photos list
            List<Long> photoIds = player.getPhotosIds();
            if (photoIds == null) {
                photoIds = new ArrayList<>();
            }
            photoIds.add(uploadedFile.getId().longValue());
            player.setPhotosIds(photoIds);
            
            Player updatedPlayer = playerDao.updatePlayer(player);
            logger.info("Successfully added photo for player: {} {}", 
                       updatedPlayer.getFirstName(), updatedPlayer.getLastName());
            return updatedPlayer;
            
        } catch (Exception e) {
            logger.error("Failed to add photo for player ID: {}", playerId, e);
            throw new RuntimeException("Failed to upload player photo", e);
        }
    }

    @Override
    @Transactional
    public Player deletePlayerMainPhoto(Long playerId) {
        logger.info("Deleting main photo for player ID: {}", playerId);
        
        Player player = playerDao.getPlayerById(playerId);
        
        if (player.getMainPhotoId() != null) {
            try {
                fileUploadService.deleteFile(player.getMainPhotoId().intValue());
            } catch (Exception e) {
                logger.warn("Failed to delete main photo file for player ID: {}", playerId, e);
            }
            
            player.setMainPhotoId(null);
            
            Player updatedPlayer = playerDao.updatePlayer(player);
            logger.info("Successfully deleted main photo for player: {} {}", 
                       updatedPlayer.getFirstName(), updatedPlayer.getLastName());
            return updatedPlayer;
        }
        
        return player;
    }

    @Override
    @Transactional
    public Player deletePlayerPhoto(Long playerId, Long photoId) {
        logger.info("Deleting photo {} for player ID: {}", photoId, playerId);
        
        Player player = playerDao.getPlayerById(playerId);
        
        List<Long> photoIds = player.getPhotosIds();
        if (photoIds != null && photoIds.contains(photoId)) {
            try {
                fileUploadService.deleteFile(photoId.intValue());
            } catch (Exception e) {
                logger.warn("Failed to delete photo file {} for player ID: {}", photoId, playerId, e);
            }
            
            photoIds.remove(photoId);
            player.setPhotosIds(photoIds);
            
            Player updatedPlayer = playerDao.updatePlayer(player);
            logger.info("Successfully deleted photo for player: {} {}", 
                       updatedPlayer.getFirstName(), updatedPlayer.getLastName());
            return updatedPlayer;
        }
        
        return player;
    }

    @Override
    public List<Player> searchPlayersByName(String query, PageRequest pageRequest) {
        logger.debug("Searching players by name: {}", query);
        return playerDao.searchPlayersByName(query, pageRequest.getPageNumber() * pageRequest.getPageSize(), 
                                           pageRequest.getPageSize());
    }

    @Override
    public List<Player> getPlayersByPosition(Player.Position position, PageRequest pageRequest) {
        logger.debug("Fetching players by position: {}", position);
        return playerDao.getPlayersByPosition(position, pageRequest.getPageNumber() * pageRequest.getPageSize(), 
                                            pageRequest.getPageSize());
    }

    @Override
    public List<Player> getPlayersByNationality(String nationality, PageRequest pageRequest) {
        logger.debug("Fetching players by nationality: {}", nationality);
        return playerDao.getPlayersByNationality(nationality, pageRequest.getPageNumber() * pageRequest.getPageSize(), 
                                                pageRequest.getPageSize());
    }

    @Override
    public List<Player> getPlayersByAgeRange(int minAge, int maxAge, PageRequest pageRequest) {
        logger.debug("Fetching players by age range: {} to {}", minAge, maxAge);
        
        LocalDate today = LocalDate.now();
        LocalDate maxBirthDate = today.minusYears(minAge);
        LocalDate minBirthDate = today.minusYears(maxAge + 1);
        
        return playerDao.getPlayersByBirthDateRange(minBirthDate, maxBirthDate, 
                                                  pageRequest.getPageNumber() * pageRequest.getPageSize(), 
                                                  pageRequest.getPageSize());
    }

    @Override
    public List<Player> getFreeAgents(PageRequest pageRequest) {
        logger.debug("Fetching free agents");
        return playerDao.getFreeAgents(pageRequest.getPageNumber() * pageRequest.getPageSize(), 
                                     pageRequest.getPageSize());
    }

    @Override
    public List<Player> getPlayersByClub(Long clubId, PageRequest pageRequest) {
        logger.debug("Fetching players by club ID: {}", clubId);
        return playerDao.getPlayersByClubId(clubId);
    }

    @Override
    public List<Player> getAllActivePlayers(PageRequest pageRequest) {
        logger.debug("Fetching all active players");
        return playerDao.getPlayersWithActiveClubs(pageRequest.getPageNumber() * pageRequest.getPageSize(), 
                                                  pageRequest.getPageSize());
    }

    @Override
    public Optional<ClubMembership> getCurrentClubMembership(Long playerId) {
        logger.debug("Getting current club membership for player ID: {}", playerId);
        return playerDao.getCurrentClubMembership(playerId);
    }

    @Override
    public List<ClubMembership> getPlayerClubHistory(Long playerId) {
        logger.debug("Getting club history for player ID: {}", playerId);
        return playerDao.getPlayerClubMemberships(playerId);
    }

    @Override
    public Map<String, Object> getPlayerStatistics(Long playerId) {
        logger.debug("Getting statistics for player ID: {}", playerId);
        
        Player player = playerDao.getPlayerById(playerId);
        List<ClubMembership> memberships = playerDao.getPlayerClubMemberships(playerId);
        List<PlayerTransfer> transfers = playerDao.getPlayerTransferHistory(playerId);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("playerId", playerId);
        stats.put("playerName", player.getFirstName() + " " + player.getLastName());
        stats.put("age", calculateAge(player.getDateOfBirth()));
        stats.put("totalClubs", memberships.size());
        stats.put("totalTransfers", transfers.size());
        stats.put("marketValue", player.getMarketValue());
        stats.put("currentClub", getCurrentClubMembership(playerId).map(cm -> cm.getClub().getName()).orElse("Free Agent"));
        stats.put("careerStart", memberships.isEmpty() ? null : 
                 memberships.get(memberships.size() - 1).getJoinedDate());
        
        return stats;
    }

    @Override
    public List<PlayerTransfer> getPlayerTransferHistory(Long playerId) {
        logger.debug("Getting transfer history for player ID: {}", playerId);
        return playerDao.getPlayerTransferHistory(playerId);
    }

    @Override
    public boolean isPlayerAvailableForTransfer(Long playerId) {
        logger.debug("Checking if player ID: {} is available for transfer", playerId);
        return !playerDao.hasPlayerPendingTransfer(playerId);
    }

    @Override
    @Transactional
    public Player deactivatePlayer(Long playerId) {
        logger.info("Deactivating player ID: {}", playerId);
        
        Player player = playerDao.getPlayerById(playerId);
        player.setIsActive(false);
        
        Player updatedPlayer = playerDao.updatePlayer(player);
        logger.info("Successfully deactivated player: {} {}", 
                   updatedPlayer.getFirstName(), updatedPlayer.getLastName());
        return updatedPlayer;
    }

    @Override
    @Transactional
    public Player reactivatePlayer(Long playerId) {
        logger.info("Reactivating player ID: {}", playerId);
        
        Player player = playerDao.getPlayerById(playerId);
        player.setIsActive(true);
        
        Player updatedPlayer = playerDao.updatePlayer(player);
        logger.info("Successfully reactivated player: {} {}", 
                   updatedPlayer.getFirstName(), updatedPlayer.getLastName());
        return updatedPlayer;
    }

    @Override
    @Transactional
    public void deletePlayer(Long playerId) {
        logger.info("Deleting player ID: {}", playerId);
        playerDao.deletePlayer(playerId);
    }

    @Override
    public Optional<Player> getPlayerByEmail(String email) {
        logger.debug("Finding player by email: {}", email);
        return playerDao.getPlayerByEmail(email);
    }

    @Override
    public Optional<Player> getPlayerByFkfRegistrationNumber(String fkfRegistrationNumber) {
        logger.debug("Finding player by FKF registration number: {}", fkfRegistrationNumber);
        return playerDao.getPlayerByFkfRegistrationNumber(fkfRegistrationNumber);
    }

    @Override
    public boolean isEmailAvailable(String email) {
        return !playerDao.existsByEmail(email.trim().toLowerCase());
    }

    @Override
    public boolean isFkfRegistrationNumberAvailable(String fkfRegistrationNumber) {
        return !playerDao.existsByFkfRegistrationNumber(fkfRegistrationNumber.trim());
    }

    @Override
    public List<Player> getRecentlyRegisteredPlayers(int limit) {
        logger.debug("Fetching recently registered players (limit: {})", limit);
        return playerDao.getRecentlyRegisteredPlayers(limit);
    }

    @Override
    public List<Player> getTopValuedPlayers(int limit) {
        logger.debug("Fetching top valued players (limit: {})", limit);
        return playerDao.getTopValuedPlayers(limit);
    }

    @Override
    public List<Player> getPlayersByPreferredFoot(Player.PreferredFoot preferredFoot, PageRequest pageRequest) {
        logger.debug("Fetching players by preferred foot: {}", preferredFoot);
        return playerDao.getPlayersByPreferredFoot(preferredFoot, 
                                                  pageRequest.getPageNumber() * pageRequest.getPageSize(), 
                                                  pageRequest.getPageSize());
    }

    @Override
    public List<Player> getPlayersByHeightRange(Double minHeight, Double maxHeight, PageRequest pageRequest) {
        logger.debug("Fetching players by height range: {} to {} cm", minHeight, maxHeight);
        return playerDao.getPlayersByHeightRange(minHeight, maxHeight,
                                                pageRequest.getPageNumber() * pageRequest.getPageSize(), 
                                                pageRequest.getPageSize());
    }

    // Private utility methods

    private void validatePlayerRegistrationInput(String firstName, String lastName, LocalDate dateOfBirth, 
                                               String nationality, Player.Position primaryPosition, 
                                               String email, String phoneNumber) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth is required");
        }
        if (nationality == null || nationality.trim().isEmpty()) {
            throw new IllegalArgumentException("Nationality is required");
        }
        if (primaryPosition == null) {
            throw new IllegalArgumentException("Primary position is required");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        // Validate age (must be between 12 and 50)
        int age = calculateAge(dateOfBirth);
        if (age < 12 || age > 50) {
            throw new IllegalArgumentException("Player age must be between 12 and 50 years");
        }
        
        // Validate email format
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        // Validate phone number format (if provided)
        if (phoneNumber != null && !phoneNumber.trim().isEmpty() && !isValidKenyanPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Invalid Kenyan phone number format");
        }
    }

    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private boolean isValidKenyanPhoneNumber(String phoneNumber) {
        // Kenyan phone number pattern: +254XXXXXXXXX or 07XXXXXXXX or 01XXXXXXXX
        return phoneNumber.matches("^(\\+254|0)[17]\\d{8}$");
    }
}