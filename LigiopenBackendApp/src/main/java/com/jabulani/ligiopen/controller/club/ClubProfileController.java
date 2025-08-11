package com.jabulani.ligiopen.controller.club;

import com.jabulani.ligiopen.dto.club.ClubDto;
import com.jabulani.ligiopen.dto.club.ClubUpdateDto;
import com.jabulani.ligiopen.entity.club.Club;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public interface ClubProfileController {

    /**
     * Get club by ID
     */
    ResponseEntity<Object> getClubById(Long clubId);

    /**
     * Update club profile (owner/manager only)
     */
    ResponseEntity<Object> updateClubProfile(Long clubId, ClubUpdateDto updateDto);

    /**
     * Upload club logo (owner/manager only)
     */
    ResponseEntity<Object> uploadClubLogo(Long clubId, MultipartFile logoFile);

    /**
     * Upload club main photo (owner/manager only)
     */
    ResponseEntity<Object> uploadClubMainPhoto(Long clubId, MultipartFile photoFile);

    /**
     * Delete club logo (owner/manager only)
     */
    ResponseEntity<Object> deleteClubLogo(Long clubId);

    /**
     * Delete club main photo (owner/manager only)
     */
    ResponseEntity<Object> deleteClubMainPhoto(Long clubId);

    /**
     * Search clubs by name
     */
    ResponseEntity<Object> searchClubsByName(String query, int page, int size);

    /**
     * Get clubs by county
     */
    ResponseEntity<Object> getClubsByCounty(Long countyId, int page, int size);

    /**
     * Get clubs by region
     */
    ResponseEntity<Object> getClubsByRegion(String region, int page, int size);

    /**
     * Get clubs by level
     */
    ResponseEntity<Object> getClubsByLevel(Club.ClubLevel level, int page, int size);

    /**
     * Get clubs near location (geographic search)
     */
    ResponseEntity<Object> getClubsNearLocation(BigDecimal latitude, BigDecimal longitude, 
                                              double radiusKm, int page, int size);

    /**
     * Get verified clubs only
     */
    ResponseEntity<Object> getVerifiedClubs(String verificationType, int page, int size);

    /**
     * Get user's owned clubs
     */
    ResponseEntity<Object> getUserOwnedClubs();

    /**
     * Get user's managed clubs
     */
    ResponseEntity<Object> getUserManagedClubs();

    /**
     * Get club statistics for detailed view
     */
    ResponseEntity<Object> getClubStatistics(Long clubId);

    /**
     * Get all active clubs (public endpoint)
     */
    ResponseEntity<Object> getAllActiveClubs(int page, int size);

    /**
     * Get club by registration number (for verification purposes)
     */
    ResponseEntity<Object> getClubByRegistrationNumber(String registrationNumber);
}