package com.jabulani.ligiopen.service.player;

import com.jabulani.ligiopen.entity.player.Player;
import com.jabulani.ligiopen.entity.player.ClubMembership;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PlayerService {
    
    /**
     * Register a new player
     * @param firstName Player's first name
     * @param lastName Player's last name
     * @param dateOfBirth Player's date of birth
     * @param nationality Player's nationality
     * @param primaryPosition Player's primary position
     * @param email Player's contact email
     * @param phoneNumber Player's contact phone
     * @return Created Player entity
     */
    Player registerPlayer(String firstName, String lastName, LocalDate dateOfBirth, 
                          String nationality, Player.Position primaryPosition, 
                          String email, String phoneNumber);
    
    /**
     * Get player by ID
     * @param playerId Player ID
     * @return Player entity if found
     */
    Optional<Player> getPlayerById(Long playerId);
    
    /**
     * Get player by ID or throw exception
     * @param playerId Player ID
     * @return Player entity
     * @throws RuntimeException if player not found
     */
    Player getPlayerByIdOrThrow(Long playerId);
    
    /**
     * Update player profile information
     * @param playerId Player ID
     * @param firstName First name
     * @param lastName Last name
     * @param nickname Nickname
     * @param dateOfBirth Date of birth
     * @param nationality Nationality
     * @param secondNationality Second nationality (optional)
     * @param heightCm Height in centimeters
     * @param weightKg Weight in kilograms
     * @param preferredFoot Preferred foot
     * @param primaryPosition Primary position
     * @param secondaryPositions Secondary positions
     * @param bio Player biography
     * @param phoneNumber Phone number
     * @param email Email address
     * @param emergencyContactName Emergency contact name
     * @param emergencyContactPhone Emergency contact phone
     * @param emergencyContactRelationship Emergency contact relationship
     * @param idNumber National ID number
     * @param passportNumber Passport number
     * @param fkfRegistrationNumber FKF registration number
     * @param marketValue Market value
     * @return Updated Player entity
     */
    Player updatePlayer(Long playerId, String firstName, String lastName, String nickname,
                       LocalDate dateOfBirth, String nationality, String secondNationality,
                       Double heightCm, Double weightKg, Player.PreferredFoot preferredFoot,
                       Player.Position primaryPosition, List<Player.Position> secondaryPositions,
                       String bio, String phoneNumber, String email, String emergencyContactName,
                       String emergencyContactPhone, String emergencyContactRelationship,
                       String idNumber, String passportNumber, String fkfRegistrationNumber,
                       BigDecimal marketValue);
    
    /**
     * Upload player main photo
     * @param playerId Player ID
     * @param photoFile Photo file
     * @return Updated Player entity
     */
    Player updatePlayerMainPhoto(Long playerId, MultipartFile photoFile);
    
    /**
     * Add additional photo to player
     * @param playerId Player ID
     * @param photoFile Photo file
     * @return Updated Player entity
     */
    Player addPlayerPhoto(Long playerId, MultipartFile photoFile);
    
    /**
     * Delete player main photo
     * @param playerId Player ID
     * @return Updated Player entity
     */
    Player deletePlayerMainPhoto(Long playerId);
    
    /**
     * Delete player photo
     * @param playerId Player ID
     * @param photoId Photo ID to delete
     * @return Updated Player entity
     */
    Player deletePlayerPhoto(Long playerId, Long photoId);
    
    /**
     * Search players by name
     * @param query Search query
     * @param pageRequest Pagination information
     * @return List of matching players
     */
    List<Player> searchPlayersByName(String query, PageRequest pageRequest);
    
    /**
     * Get players by position
     * @param position Player position
     * @param pageRequest Pagination information
     * @return List of players in specified position
     */
    List<Player> getPlayersByPosition(Player.Position position, PageRequest pageRequest);
    
    /**
     * Get players by nationality
     * @param nationality Player nationality
     * @param pageRequest Pagination information
     * @return List of players with specified nationality
     */
    List<Player> getPlayersByNationality(String nationality, PageRequest pageRequest);
    
    /**
     * Get players by age range
     * @param minAge Minimum age
     * @param maxAge Maximum age
     * @param pageRequest Pagination information
     * @return List of players within age range
     */
    List<Player> getPlayersByAgeRange(int minAge, int maxAge, PageRequest pageRequest);
    
    /**
     * Get free agents (players without active club membership)
     * @param pageRequest Pagination information
     * @return List of free agent players
     */
    List<Player> getFreeAgents(PageRequest pageRequest);
    
    /**
     * Get players by club
     * @param clubId Club ID
     * @param pageRequest Pagination information
     * @return List of players in the club
     */
    List<Player> getPlayersByClub(Long clubId, PageRequest pageRequest);
    
    /**
     * Get all active players
     * @param pageRequest Pagination information
     * @return List of active players
     */
    List<Player> getAllActivePlayers(PageRequest pageRequest);
    
    /**
     * Get player's current club membership
     * @param playerId Player ID
     * @return Current club membership if exists
     */
    Optional<ClubMembership> getCurrentClubMembership(Long playerId);
    
    /**
     * Get player's club membership history
     * @param playerId Player ID
     * @return List of all club memberships
     */
    List<ClubMembership> getPlayerClubHistory(Long playerId);
    
    /**
     * Get player statistics
     * @param playerId Player ID
     * @return Player statistics map
     */
    Map<String, Object> getPlayerStatistics(Long playerId);
    
    /**
     * Get player transfer history
     * @param playerId Player ID
     * @return List of player transfers
     */
    List<com.jabulani.ligiopen.entity.player.PlayerTransfer> getPlayerTransferHistory(Long playerId);
    
    /**
     * Check if player is available for transfer
     * @param playerId Player ID
     * @return True if player is available for transfer
     */
    boolean isPlayerAvailableForTransfer(Long playerId);
    
    /**
     * Deactivate player
     * @param playerId Player ID
     * @return Updated Player entity
     */
    Player deactivatePlayer(Long playerId);
    
    /**
     * Reactivate player
     * @param playerId Player ID
     * @return Updated Player entity
     */
    Player reactivatePlayer(Long playerId);
    
    /**
     * Delete player (soft delete)
     * @param playerId Player ID
     */
    void deletePlayer(Long playerId);
    
    /**
     * Get player by email
     * @param email Player email
     * @return Player entity if found
     */
    Optional<Player> getPlayerByEmail(String email);
    
    /**
     * Get player by FKF registration number
     * @param fkfRegistrationNumber FKF registration number
     * @return Player entity if found
     */
    Optional<Player> getPlayerByFkfRegistrationNumber(String fkfRegistrationNumber);
    
    /**
     * Check if email is available for registration
     * @param email Email to check
     * @return True if email is available
     */
    boolean isEmailAvailable(String email);
    
    /**
     * Check if FKF registration number is available
     * @param fkfRegistrationNumber FKF registration number to check
     * @return True if registration number is available
     */
    boolean isFkfRegistrationNumberAvailable(String fkfRegistrationNumber);
    
    /**
     * Get recently registered players
     * @param limit Number of recent players to return
     * @return List of recently registered players
     */
    List<Player> getRecentlyRegisteredPlayers(int limit);
    
    /**
     * Get top valued players
     * @param limit Number of top players to return
     * @return List of top valued players
     */
    List<Player> getTopValuedPlayers(int limit);
    
    /**
     * Get players by preferred foot
     * @param preferredFoot Preferred foot (LEFT, RIGHT, BOTH)
     * @param pageRequest Pagination information
     * @return List of players with specified preferred foot
     */
    List<Player> getPlayersByPreferredFoot(Player.PreferredFoot preferredFoot, PageRequest pageRequest);
    
    /**
     * Get players by height range
     * @param minHeight Minimum height in centimeters
     * @param maxHeight Maximum height in centimeters
     * @param pageRequest Pagination information
     * @return List of players within height range
     */
    List<Player> getPlayersByHeightRange(Double minHeight, Double maxHeight, PageRequest pageRequest);
}