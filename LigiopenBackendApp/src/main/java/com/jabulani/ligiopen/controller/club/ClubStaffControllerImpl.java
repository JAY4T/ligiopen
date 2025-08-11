package com.jabulani.ligiopen.controller.club;

import com.jabulani.ligiopen.config.web.BuildResponse;
import com.jabulani.ligiopen.dto.club.ClubDto;
import com.jabulani.ligiopen.dto.club.ClubMapper;
import com.jabulani.ligiopen.dto.user.UserDto;
import com.jabulani.ligiopen.mapper.UserMapper;
import com.jabulani.ligiopen.entity.club.Club;
import com.jabulani.ligiopen.entity.club.ClubStaffMember;
import com.jabulani.ligiopen.entity.user.UserEntity;
import com.jabulani.ligiopen.service.club.ClubService;
import com.jabulani.ligiopen.service.user.UserEntityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/clubs/{clubId}/staff")
@Tag(name = "Club Staff Management", description = "APIs for managing club staff, managers, and ownership")
@SecurityRequirement(name = "bearerAuth")
public class ClubStaffControllerImpl implements ClubStaffController {

    private static final Logger logger = LoggerFactory.getLogger(ClubStaffControllerImpl.class);

    private final ClubService clubService;
    private final UserEntityService userEntityService;
    private final UserMapper userMapper;
    private final ClubMapper clubMapper;
    private final BuildResponse buildResponse;

    @Autowired
    public ClubStaffControllerImpl(ClubService clubService, UserEntityService userEntityService,
                                 UserMapper userMapper, ClubMapper clubMapper, BuildResponse buildResponse) {
        this.clubService = clubService;
        this.userEntityService = userEntityService;
        this.userMapper = userMapper;
        this.clubMapper = clubMapper;
        this.buildResponse = buildResponse;
    }

    @Operation(summary = "Add club manager", 
               description = "Add a new manager to the club (owner only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Manager added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid manager ID or already a manager"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "403", description = "User not authorized (must be club owner)"),
        @ApiResponse(responseCode = "404", description = "Club or user not found")
    })
    @PostMapping("/managers/{managerId}")
    @Override
    public ResponseEntity<Object> addClubManager(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId,
            @Parameter(description = "Manager user ID", required = true) @PathVariable Long managerId) {
        try {
            Long ownerId = getCurrentUserId();
            logger.info("Adding manager ID: {} to club ID: {} by owner ID: {}", managerId, clubId, ownerId);

            Club club = clubService.addClubManager(clubId, ownerId, managerId);
            ClubDto clubDto = clubMapper.toClubDto(club);

            return buildResponse.success(clubDto, "Manager added successfully");

        } catch (Exception e) {
            logger.error("Failed to add manager ID: {} to club ID: {}", managerId, clubId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("manager", e.getMessage());
            return buildResponse.error("Failed to add manager", errors);
        }
    }

    @Operation(summary = "Remove club manager", 
               description = "Remove a manager from the club (owner only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Manager removed successfully"),
        @ApiResponse(responseCode = "400", description = "Manager not found in club"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "403", description = "User not authorized (must be club owner)"),
        @ApiResponse(responseCode = "404", description = "Club or manager not found")
    })
    @DeleteMapping("/managers/{managerId}")
    @Override
    public ResponseEntity<Object> removeClubManager(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId,
            @Parameter(description = "Manager user ID", required = true) @PathVariable Long managerId) {
        try {
            Long ownerId = getCurrentUserId();
            logger.info("Removing manager ID: {} from club ID: {} by owner ID: {}", managerId, clubId, ownerId);

            Club club = clubService.removeClubManager(clubId, ownerId, managerId);
            ClubDto clubDto = clubMapper.toClubDto(club);

            return buildResponse.success(clubDto, "Manager removed successfully");

        } catch (Exception e) {
            logger.error("Failed to remove manager ID: {} from club ID: {}", managerId, clubId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("manager", e.getMessage());
            return buildResponse.error("Failed to remove manager", errors);
        }
    }

    @Operation(summary = "Get club managers", 
               description = "Get all managers of the club")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Managers retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Club not found")
    })
    @GetMapping("/managers")
    @Override
    public ResponseEntity<Object> getClubManagers(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId) {
        try {
            logger.info("Getting managers for club ID: {}", clubId);

            Club club = clubService.getClubById(clubId);
            // Note: Club.managers is List<UserEntity>, not ClubStaffMember
            List<UserDto> managers = club.getManagers().stream()
                    .map(userMapper::toUserDto)
                    .toList();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("managers", managers);
            responseData.put("count", managers.size());
            responseData.put("clubId", clubId);
            responseData.put("clubName", club.getName());

            return buildResponse.success(responseData, "Club managers retrieved successfully");

        } catch (Exception e) {
            logger.error("Failed to get managers for club ID: {}", clubId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("managers", e.getMessage());
            return buildResponse.error("Failed to get club managers", errors);
        }
    }

    @Operation(summary = "Transfer club ownership", 
               description = "Transfer club ownership to another user (current owner only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ownership transferred successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid new owner ID"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "403", description = "User not authorized (must be current owner)"),
        @ApiResponse(responseCode = "404", description = "Club or new owner not found")
    })
    @PostMapping("/transfer-ownership/{newOwnerId}")
    @Override
    public ResponseEntity<Object> transferClubOwnership(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId,
            @Parameter(description = "New owner user ID", required = true) @PathVariable Long newOwnerId) {
        try {
            Long currentOwnerId = getCurrentUserId();
            logger.info("Transferring ownership of club ID: {} from owner ID: {} to new owner ID: {}", 
                       clubId, currentOwnerId, newOwnerId);

            Club club = clubService.transferOwnership(clubId, currentOwnerId, newOwnerId);
            ClubDto clubDto = clubMapper.toClubDto(club);

            return buildResponse.success(clubDto, "Club ownership transferred successfully");

        } catch (Exception e) {
            logger.error("Failed to transfer ownership of club ID: {} to user ID: {}", clubId, newOwnerId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("ownership", e.getMessage());
            return buildResponse.error("Failed to transfer ownership", errors);
        }
    }

    @Operation(summary = "Get club ownership history", 
               description = "Get the ownership history of the club")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ownership history retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Club not found")
    })
    @GetMapping("/ownership-history")
    @Override
    public ResponseEntity<Object> getClubOwnershipHistory(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId) {
        try {
            logger.info("Getting ownership history for club ID: {}", clubId);

            // For now, we'll just return current owner info
            // In a full implementation, this would query an ownership history table
            Club club = clubService.getClubById(clubId);
            
            Map<String, Object> currentOwnership = new HashMap<>();
            currentOwnership.put("ownerId", club.getOwner().getId());
            currentOwnership.put("ownerName", club.getOwner().getFirstName() + " " + club.getOwner().getLastName());
            currentOwnership.put("ownerEmail", club.getOwner().getEmail());
            currentOwnership.put("ownershipStart", club.getCreatedAt()); // Simplified - would be actual ownership start date
            currentOwnership.put("isCurrent", true);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("currentOwner", currentOwnership);
            responseData.put("history", List.of(currentOwnership)); // Would be full history in real implementation
            responseData.put("clubId", clubId);
            responseData.put("clubName", club.getName());

            return buildResponse.success(responseData, "Ownership history retrieved successfully");

        } catch (Exception e) {
            logger.error("Failed to get ownership history for club ID: {}", clubId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("ownership", e.getMessage());
            return buildResponse.error("Failed to get ownership history", errors);
        }
    }

    @Operation(summary = "Get club staff", 
               description = "Get complete club staff information (owner and managers)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Club staff retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Club not found")
    })
    @GetMapping("")
    @Override
    public ResponseEntity<Object> getClubStaff(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId) {
        try {
            logger.info("Getting complete staff for club ID: {}", clubId);

            Club club = clubService.getClubById(clubId);
            
            // Owner information
            UserDto owner = userMapper.toUserDto(club.getOwner());
            
            // Managers information
            List<UserDto> managers = club.getManagers().stream()
                    .map(userMapper::toUserDto)
                    .toList();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("owner", owner);
            responseData.put("managers", managers);
            responseData.put("managersCount", managers.size());
            responseData.put("clubId", clubId);
            responseData.put("clubName", club.getName());

            return buildResponse.success(responseData, "Club staff retrieved successfully");

        } catch (Exception e) {
            logger.error("Failed to get staff for club ID: {}", clubId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("staff", e.getMessage());
            return buildResponse.error("Failed to get club staff", errors);
        }
    }

    // Placeholder implementations for invitation system (would require additional entities)

    @Operation(summary = "Update manager role", 
               description = "Update manager role/permissions (owner only)")
    @PostMapping("/managers/{managerId}/role")
    @Override
    public ResponseEntity<Object> updateManagerRole(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId,
            @Parameter(description = "Manager user ID", required = true) @PathVariable Long managerId,
            @Parameter(description = "New role", required = true) @RequestParam String role) {
        try {
            logger.info("Updating role for manager ID: {} in club ID: {} to role: {}", managerId, clubId, role);
            
            // This would require additional implementation in the service layer
            Map<String, Object> errors = new HashMap<>();
            errors.put("feature", "Manager role update feature not yet implemented");
            return buildResponse.error("Feature not implemented", errors);

        } catch (Exception e) {
            logger.error("Failed to update role for manager ID: {} in club ID: {}", managerId, clubId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("role", e.getMessage());
            return buildResponse.error("Failed to update manager role", errors);
        }
    }

    @Operation(summary = "Invite club manager", 
               description = "Send invitation to user to become club manager (owner only)")
    @PostMapping("/managers/invite")
    @Override
    public ResponseEntity<Object> inviteClubManager(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId,
            @Parameter(description = "User email to invite", required = true) @RequestParam String email,
            @Parameter(description = "Manager role", required = true) @RequestParam String role) {
        try {
            logger.info("Inviting user with email: {} to be manager of club ID: {} with role: {}", email, clubId, role);
            
            // This would require additional implementation for invitation system
            Map<String, Object> errors = new HashMap<>();
            errors.put("feature", "Manager invitation feature not yet implemented");
            return buildResponse.error("Feature not implemented", errors);

        } catch (Exception e) {
            logger.error("Failed to invite manager with email: {} for club ID: {}", email, clubId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("invitation", e.getMessage());
            return buildResponse.error("Failed to send invitation", errors);
        }
    }

    @PostMapping("/invitations/{invitationId}/accept")
    @Override
    public ResponseEntity<Object> acceptManagerInvitation(@PathVariable Long invitationId) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("feature", "Manager invitation acceptance feature not yet implemented");
        return buildResponse.error("Feature not implemented", errors);
    }

    @PostMapping("/invitations/{invitationId}/decline")
    @Override
    public ResponseEntity<Object> declineManagerInvitation(@PathVariable Long invitationId) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("feature", "Manager invitation decline feature not yet implemented");
        return buildResponse.error("Feature not implemented", errors);
    }

    @GetMapping("/invitations/pending")
    @Override
    public ResponseEntity<Object> getPendingInvitations(@PathVariable Long clubId) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("feature", "Pending invitations feature not yet implemented");
        return buildResponse.error("Feature not implemented", errors);
    }

    @DeleteMapping("/invitations/{invitationId}")
    @Override
    public ResponseEntity<Object> cancelManagerInvitation(@PathVariable Long invitationId) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("feature", "Cancel invitation feature not yet implemented");
        return buildResponse.error("Feature not implemented", errors);
    }

    /**
     * Get the current authenticated user's ID from the security context
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        
        String principal = authentication.getName();
        
        try {
            var userDto = userEntityService.getUserByEmail(principal);
            return userDto.getId();
        } catch (Exception e) {
            try {
                return Long.parseLong(principal);
            } catch (NumberFormatException ex) {
                throw new RuntimeException("Unable to determine current user ID");
            }
        }
    }
}