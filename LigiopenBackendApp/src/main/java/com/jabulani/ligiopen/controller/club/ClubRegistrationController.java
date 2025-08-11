package com.jabulani.ligiopen.controller.club;

import com.jabulani.ligiopen.dto.club.ClubRegistrationDto;
import com.jabulani.ligiopen.dto.club.ClubVerificationDto;
import com.jabulani.ligiopen.entity.club.Club;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public interface ClubRegistrationController {

    /**
     * Register a grassroots club
     */
    ResponseEntity<Object> registerGrassrootsClub(ClubRegistrationDto registrationDto);

    /**
     * Register a club with FKF registration number
     */
    ResponseEntity<Object> registerFkfClub(ClubRegistrationDto registrationDto);

    /**
     * Add FKF registration to existing grassroots club
     */
    ResponseEntity<Object> addFkfRegistration(Long clubId, String registrationNumber, 
                                            Club.ClubLevel clubLevel, LocalDate founded);

    /**
     * Submit club for LigiOpen verification (club owner only)
     */
    ResponseEntity<Object> submitForVerification(Long clubId);

    /**
     * LigiOpen admin verify club
     */
    ResponseEntity<Object> ligiopenVerifyClub(Long clubId, ClubVerificationDto verificationDto);

    /**
     * Update FKF verification status (admin only)
     */
    ResponseEntity<Object> updateFkfVerificationStatus(Long clubId, Club.FkfVerificationStatus status);

    /**
     * Suspend club (admin only)
     */
    ResponseEntity<Object> suspendClub(Long clubId, String reason);

    /**
     * Reactivate club (admin only)
     */
    ResponseEntity<Object> reactivateClub(Long clubId);

    /**
     * Get clubs pending LigiOpen verification (admin only)
     */
    ResponseEntity<Object> getClubsPendingVerification();

    /**
     * Get verification statistics (admin only)
     */
    ResponseEntity<Object> getVerificationStatistics();
}