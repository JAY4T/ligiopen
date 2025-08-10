package com.jabulani.ligiopen.dao;

import com.jabulani.ligiopen.entity.club.Club;
import com.jabulani.ligiopen.entity.location.County;
import com.jabulani.ligiopen.entity.user.UserEntity;

import java.util.List;
import java.util.Optional;

public interface ClubDao {
    
    Club createClub(Club club);
    
    Club updateClub(Club club);
    
    Club getClubById(Long id);
    
    Optional<Club> findClubById(Long id);
    
    Optional<Club> getClubByName(String name);
    
    Optional<Club> getClubByRegistrationNumber(String registrationNumber);
    
    List<Club> getAllClubs();
    
    List<Club> getActiveClubs();
    
    List<Club> getClubsByCounty(County county);
    
    List<Club> getClubsByCountyId(Long countyId);
    
    List<Club> getClubsByLevel(Club.ClubLevel clubLevel);
    
    List<Club> getClubsByOwner(UserEntity owner);
    
    List<Club> getClubsByOwnerId(Long ownerId);
    
    List<Club> getClubsManagedByUser(UserEntity user);
    
    List<Club> getClubsManagedByUserId(Long userId);
    
    /**
     * Get clubs by LigiOpen verification status
     */
    List<Club> getClubsByLigiopenVerificationStatus(Club.LigiopenVerificationStatus status);
    
    /**
     * Get clubs by FKF verification status  
     */
    List<Club> getClubsByFkfVerificationStatus(Club.FkfVerificationStatus status);
    
    /**
     * Get clubs that are verified by LigiOpen
     */
    List<Club> getLigiopenVerifiedClubs();
    
    /**
     * Get clubs that are verified by FKF
     */
    List<Club> getFkfVerifiedClubs();
    
    /**
     * Get clubs that are verified by both LigiOpen and FKF
     */
    List<Club> getFullyVerifiedClubs();
    
    /**
     * Get clubs pending LigiOpen verification
     */
    List<Club> getClubsPendingLigiopenVerification();
    
    /**
     * Get clubs with FKF registration numbers
     */
    List<Club> getClubsWithFkfRegistration();
    
    /**
     * Get grassroots clubs (no FKF registration needed)
     */
    List<Club> getGrassrootsClubs();
    
    /**
     * Search clubs by name
     */
    List<Club> searchClubsByName(String searchTerm);
    
    /**
     * Get clubs favorited by user
     */
    List<Club> getClubsFavoritedByUser(UserEntity user);
    
    List<Club> getClubsFavoritedByUserId(Long userId);
    
    /**
     * Get recently created clubs
     */
    List<Club> getRecentlyCreatedClubs(int limit);
    
    /**
     * Get clubs by region (through county)
     */
    List<Club> getClubsByRegion(String region);
    
    void deleteClub(Long id);
    
    boolean existsByName(String name);
    
    boolean existsByRegistrationNumber(String registrationNumber);
    
    boolean existsByNameAndCounty(String name, County county);
    
    boolean isUserClubOwner(Long userId, Long clubId);
    
    boolean isUserClubManager(Long userId, Long clubId);
    
    boolean hasUserFavoritedClub(Long userId, Long clubId);
    
    long countClubs();
    
    long countActiveClubs();
    
    long countClubsByCounty(Long countyId);
    
    long countClubsByLevel(Club.ClubLevel level);
    
    long countLigiopenVerifiedClubs();
    
    long countFkfVerifiedClubs();
}