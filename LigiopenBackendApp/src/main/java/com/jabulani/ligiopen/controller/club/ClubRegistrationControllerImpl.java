package com.jabulani.ligiopen.controller.club;

import com.jabulani.ligiopen.config.web.BuildResponse;
import com.jabulani.ligiopen.dto.club.ClubDto;
import com.jabulani.ligiopen.dto.club.ClubMapper;
import com.jabulani.ligiopen.dto.club.ClubRegistrationDto;
import com.jabulani.ligiopen.dto.club.ClubVerificationDto;
import com.jabulani.ligiopen.entity.club.Club;
import com.jabulani.ligiopen.service.club.ClubService;
import com.jabulani.ligiopen.service.user.UserEntityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/clubs/registration")
@Tag(name = "Club Registration & Verification", description = "APIs for club registration and verification management")
@SecurityRequirement(name = "bearerAuth")
public class ClubRegistrationControllerImpl implements ClubRegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(ClubRegistrationControllerImpl.class);

    private final ClubService clubService;
    private final UserEntityService userEntityService;
    private final ClubMapper clubMapper;
    private final BuildResponse buildResponse;

    @Autowired
    public ClubRegistrationControllerImpl(ClubService clubService, UserEntityService userEntityService,
                                        ClubMapper clubMapper, BuildResponse buildResponse) {
        this.clubService = clubService;
        this.userEntityService = userEntityService;
        this.clubMapper = clubMapper;
        this.buildResponse = buildResponse;
    }

    @Operation(summary = "Register grassroots club", 
               description = "Register a grassroots-level club without FKF registration")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Club registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid registration data"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "409", description = "Club name already exists in county")
    })
    @PostMapping("/grassroots")
    @Override
    public ResponseEntity<Object> registerGrassrootsClub(
            @Parameter(description = "Club registration details", required = true)
            @Valid @RequestBody ClubRegistrationDto registrationDto) {
        try {
            Long userId = getCurrentUserId();
            logger.info("Registering grassroots club '{}' by user ID: {}", registrationDto.getName(), userId);

            Club club = clubService.registerClub(
                registrationDto.getName(),
                registrationDto.getShortName(),
                userId,
                registrationDto.getCountyId(),
                registrationDto.getDescription(),
                registrationDto.getContactEmail(),
                registrationDto.getContactPhone()
            );

            ClubDto clubDto = clubMapper.toClubDto(club);
            return buildResponse.success(clubDto, "Grassroots club registered successfully", null, HttpStatus.CREATED);

        } catch (Exception e) {
            logger.error("Failed to register grassroots club", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("registration", e.getMessage());
            return buildResponse.error("Failed to register club", errors);
        }
    }

    @Operation(summary = "Register FKF club", 
               description = "Register a club with FKF registration number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "FKF club registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid registration data or FKF number"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "409", description = "Club name or FKF registration number already exists")
    })
    @PostMapping("/fkf")
    @Override
    public ResponseEntity<Object> registerFkfClub(
            @Parameter(description = "Club registration details with FKF information", required = true)
            @Valid @RequestBody ClubRegistrationDto registrationDto) {
        try {
            Long userId = getCurrentUserId();
            logger.info("Registering FKF club '{}' with registration number '{}' by user ID: {}", 
                       registrationDto.getName(), registrationDto.getRegistrationNumber(), userId);

            if (registrationDto.getRegistrationNumber() == null || registrationDto.getRegistrationNumber().trim().isEmpty()) {
                Map<String, Object> errors = new HashMap<>();
                errors.put("registrationNumber", "FKF registration number is required");
                return buildResponse.error("Invalid FKF registration data", errors);
            }

            if (registrationDto.getClubLevel() == null) {
                Map<String, Object> errors = new HashMap<>();
                errors.put("clubLevel", "Club level is required for FKF registered clubs");
                return buildResponse.error("Invalid FKF registration data", errors);
            }

            Club club = clubService.registerClubWithFkfNumber(
                registrationDto.getName(),
                registrationDto.getShortName(),
                userId,
                registrationDto.getCountyId(),
                registrationDto.getDescription(),
                registrationDto.getContactEmail(),
                registrationDto.getContactPhone(),
                registrationDto.getRegistrationNumber(),
                registrationDto.getClubLevel(),
                registrationDto.getFounded()
            );

            ClubDto clubDto = clubMapper.toClubDto(club);
            return buildResponse.success(clubDto, "FKF club registered successfully", null, HttpStatus.CREATED);

        } catch (Exception e) {
            logger.error("Failed to register FKF club", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("registration", e.getMessage());
            return buildResponse.error("Failed to register FKF club", errors);
        }
    }

    @Operation(summary = "Add FKF registration", 
               description = "Add FKF registration to existing grassroots club")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "FKF registration added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid FKF registration data"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "403", description = "User not authorized (must be club owner)"),
        @ApiResponse(responseCode = "409", description = "FKF registration number already exists")
    })
    @PostMapping("/{clubId}/fkf-registration")
    @Override
    public ResponseEntity<Object> addFkfRegistration(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId,
            @Parameter(description = "FKF registration number", required = true) @RequestParam String registrationNumber,
            @Parameter(description = "Club level", required = true) @RequestParam Club.ClubLevel clubLevel,
            @Parameter(description = "Foundation date") @RequestParam(required = false) LocalDate founded) {
        try {
            Long userId = getCurrentUserId();
            logger.info("Adding FKF registration '{}' to club ID: {} by user ID: {}", 
                       registrationNumber, clubId, userId);

            Club club = clubService.addFkfRegistration(clubId, userId, registrationNumber, clubLevel, founded);
            ClubDto clubDto = clubMapper.toClubDto(club);
            
            return buildResponse.success(clubDto, "FKF registration added successfully");

        } catch (Exception e) {
            logger.error("Failed to add FKF registration to club ID: {}", clubId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("fkfRegistration", e.getMessage());
            return buildResponse.error("Failed to add FKF registration", errors);
        }
    }

    @Operation(summary = "Submit for verification", 
               description = "Submit club for LigiOpen verification (club owner only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Club submitted for verification"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "403", description = "User not authorized (must be club owner)"),
        @ApiResponse(responseCode = "400", description = "Club already verified or under review")
    })
    @PostMapping("/{clubId}/submit-verification")
    @Override
    public ResponseEntity<Object> submitForVerification(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId) {
        try {
            Long userId = getCurrentUserId();
            logger.info("Submitting club ID: {} for verification by user ID: {}", clubId, userId);

            Club club = clubService.submitForLigiopenVerification(clubId, userId);
            ClubDto clubDto = clubMapper.toClubDto(club);
            
            return buildResponse.success(clubDto, "Club submitted for LigiOpen verification");

        } catch (Exception e) {
            logger.error("Failed to submit club ID: {} for verification", clubId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("verification", e.getMessage());
            return buildResponse.error("Failed to submit for verification", errors);
        }
    }

    @Operation(summary = "Verify club (Admin)", 
               description = "LigiOpen admin verify or reject club")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Club verification status updated"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "403", description = "User not authorized (admin only)")
    })
    @PostMapping("/{clubId}/ligiopen-verify")
    @Override
    public ResponseEntity<Object> ligiopenVerifyClub(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId,
            @Parameter(description = "Verification decision and notes", required = true)
            @Valid @RequestBody ClubVerificationDto verificationDto) {
        try {
            Long adminUserId = getCurrentUserId();
            logger.info("LigiOpen {} club ID: {} by admin ID: {}", 
                       verificationDto.getVerified() ? "verifying" : "rejecting", clubId, adminUserId);

            Club club = clubService.ligiopenVerifyClub(clubId, adminUserId, 
                                                      verificationDto.getVerified(), 
                                                      verificationDto.getNotes());
            ClubDto clubDto = clubMapper.toClubDto(club);
            
            String message = verificationDto.getVerified() ? 
                "Club verified by LigiOpen" : "Club rejected by LigiOpen";
            
            return buildResponse.success(clubDto, message);

        } catch (Exception e) {
            logger.error("Failed to process LigiOpen verification for club ID: {}", clubId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("verification", e.getMessage());
            return buildResponse.error("Failed to process verification", errors);
        }
    }

    @Operation(summary = "Update FKF verification (Admin)", 
               description = "Update FKF verification status (admin only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "FKF verification status updated"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "403", description = "User not authorized (admin only)")
    })
    @PostMapping("/{clubId}/fkf-verify")
    @Override
    public ResponseEntity<Object> updateFkfVerificationStatus(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId,
            @Parameter(description = "FKF verification status", required = true) 
            @RequestParam Club.FkfVerificationStatus status) {
        try {
            Long adminUserId = getCurrentUserId();
            logger.info("Updating FKF verification status for club ID: {} to {} by admin ID: {}", 
                       clubId, status, adminUserId);

            Club club = clubService.updateFkfVerificationStatus(clubId, adminUserId, status);
            ClubDto clubDto = clubMapper.toClubDto(club);
            
            return buildResponse.success(clubDto, "FKF verification status updated");

        } catch (Exception e) {
            logger.error("Failed to update FKF verification for club ID: {}", clubId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("fkfVerification", e.getMessage());
            return buildResponse.error("Failed to update FKF verification", errors);
        }
    }

    @Operation(summary = "Suspend club (Admin)", 
               description = "Suspend club (admin only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Club suspended successfully"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "403", description = "User not authorized (admin only)")
    })
    @PostMapping("/{clubId}/suspend")
    @Override
    public ResponseEntity<Object> suspendClub(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId,
            @Parameter(description = "Suspension reason") @RequestParam(required = false) String reason) {
        try {
            Long adminUserId = getCurrentUserId();
            logger.info("Suspending club ID: {} by admin ID: {}", clubId, adminUserId);

            Club club = clubService.suspendClub(clubId, adminUserId, reason);
            ClubDto clubDto = clubMapper.toClubDto(club);
            
            return buildResponse.success(clubDto, "Club suspended successfully");

        } catch (Exception e) {
            logger.error("Failed to suspend club ID: {}", clubId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("suspension", e.getMessage());
            return buildResponse.error("Failed to suspend club", errors);
        }
    }

    @Operation(summary = "Reactivate club (Admin)", 
               description = "Reactivate suspended club (admin only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Club reactivated successfully"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "403", description = "User not authorized (admin only)")
    })
    @PostMapping("/{clubId}/reactivate")
    @Override
    public ResponseEntity<Object> reactivateClub(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId) {
        try {
            Long adminUserId = getCurrentUserId();
            logger.info("Reactivating club ID: {} by admin ID: {}", clubId, adminUserId);

            Club club = clubService.reactivateClub(clubId, adminUserId);
            ClubDto clubDto = clubMapper.toClubDto(club);
            
            return buildResponse.success(clubDto, "Club reactivated successfully");

        } catch (Exception e) {
            logger.error("Failed to reactivate club ID: {}", clubId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("reactivation", e.getMessage());
            return buildResponse.error("Failed to reactivate club", errors);
        }
    }

    @Operation(summary = "Get clubs pending verification (Admin)", 
               description = "Get all clubs pending LigiOpen verification")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clubs pending verification retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "403", description = "User not authorized (admin only)")
    })
    @GetMapping("/pending")
    @Override
    public ResponseEntity<Object> getClubsPendingVerification() {
        try {
            logger.info("Fetching clubs pending verification");

            List<Club> clubs = clubService.getClubsPendingVerification();
            List<ClubDto> clubDtos = clubs.stream()
                    .map(clubMapper::toClubDto)
                    .toList();
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("clubs", clubDtos);
            responseData.put("count", clubDtos.size());
            
            return buildResponse.success(responseData, "Clubs pending verification retrieved");

        } catch (Exception e) {
            logger.error("Failed to fetch clubs pending verification", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("pendingClubs", e.getMessage());
            return buildResponse.error("Failed to fetch pending clubs", errors);
        }
    }

    @Operation(summary = "Get verification statistics (Admin)", 
               description = "Get club verification statistics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verification statistics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "403", description = "User not authorized (admin only)")
    })
    @GetMapping("/statistics")
    @Override
    public ResponseEntity<Object> getVerificationStatistics() {
        try {
            logger.info("Fetching verification statistics");

            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalClubs", clubService.getTotalClubCount());
            statistics.put("activeClubs", clubService.getActiveClubCount());
            statistics.put("ligiopenVerified", clubService.getVerifiedClubCount("ligiopen"));
            statistics.put("fkfVerified", clubService.getVerifiedClubCount("fkf"));
            statistics.put("fullyVerified", clubService.getVerifiedClubCount("both"));
            statistics.put("grassrootsClubs", clubService.getClubCountByLevel(Club.ClubLevel.GRASSROOTS));
            statistics.put("pendingVerification", clubService.getClubsPendingVerification().size());
            
            return buildResponse.success(statistics, "Verification statistics retrieved");

        } catch (Exception e) {
            logger.error("Failed to fetch verification statistics", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("statistics", e.getMessage());
            return buildResponse.error("Failed to fetch statistics", errors);
        }
    }

    /**
     * Get the current authenticated user's ID from the security context
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        
        // The principal should be the user's email or username
        String principal = authentication.getName();
        
        // Try to get user by email first (common case for JWT)
        try {
            var userDto = userEntityService.getUserByEmail(principal);
            return userDto.getId();
        } catch (Exception e) {
            // If not found by email, try to parse as ID
            try {
                return Long.parseLong(principal);
            } catch (NumberFormatException ex) {
                throw new RuntimeException("Unable to determine current user ID");
            }
        }
    }
}