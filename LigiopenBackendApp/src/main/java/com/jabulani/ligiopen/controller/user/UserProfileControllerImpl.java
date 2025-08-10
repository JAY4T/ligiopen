package com.jabulani.ligiopen.controller.user;

import com.jabulani.ligiopen.config.web.BuildResponse;
import com.jabulani.ligiopen.dto.response.SuccessDto;
import com.jabulani.ligiopen.dto.user.UpdateUserProfileDto;
import com.jabulani.ligiopen.dto.user.UserProfileDto;
import com.jabulani.ligiopen.entity.file.File;
import com.jabulani.ligiopen.service.file.FileUploadService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Profile Management", description = "APIs for managing user profiles and personal information")
@SecurityRequirement(name = "bearerAuth")
public class UserProfileControllerImpl implements UserProfileController {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileControllerImpl.class);

    private final UserEntityService userEntityService;
    private final FileUploadService fileUploadService;
    private final BuildResponse buildResponse;

    @Autowired
    public UserProfileControllerImpl(UserEntityService userEntityService, FileUploadService fileUploadService, BuildResponse buildResponse) {
        this.userEntityService = userEntityService;
        this.fileUploadService = fileUploadService;
        this.buildResponse = buildResponse;
    }

    @Operation(summary = "Get current user profile", description = "Retrieve the authenticated user's profile information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "404", description = "User profile not found")
    })
    @GetMapping("/profile")
    @Override
    public ResponseEntity<Object> getCurrentUserProfile() {
        try {
            Long userId = getCurrentUserId();
            logger.info("Fetching profile for authenticated user ID: {}", userId);
            
            UserProfileDto profile = userEntityService.getUserProfile(userId);
            
            return buildResponse.success(profile, "Profile retrieved successfully");
        } catch (Exception e) {
            logger.error("Failed to get current user profile", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("profile", e.getMessage());
            return buildResponse.error("Failed to retrieve profile", errors);
        }
    }

    @Operation(summary = "Get user profile by ID", description = "Retrieve any user's profile information by user ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{userId}/profile")
    @Override
    public ResponseEntity<Object> getUserProfile(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long userId) {
        try {
            logger.info("Fetching profile for user ID: {}", userId);
            
            UserProfileDto profile = userEntityService.getUserProfile(userId);
            
            return buildResponse.success(profile, "Profile retrieved successfully");
        } catch (Exception e) {
            logger.error("Failed to get user profile for ID: {}", userId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("profile", e.getMessage());
            return buildResponse.error("Failed to retrieve profile", errors);
        }
    }

    @Operation(summary = "Update current user profile", description = "Update the authenticated user's profile information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "409", description = "Username or email already exists")
    })
    @PutMapping("/profile")
    @Override
    public ResponseEntity<Object> updateCurrentUserProfile(@Valid @RequestBody UpdateUserProfileDto updateDto) {
        try {
            Long userId = getCurrentUserId();
            logger.info("Updating profile for authenticated user ID: {}", userId);
            
            UserProfileDto updatedProfile = userEntityService.updateUserProfile(userId, updateDto);
            
            return buildResponse.success(updatedProfile, "Profile updated successfully");
        } catch (Exception e) {
            logger.error("Failed to update current user profile", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("profile", e.getMessage());
            return buildResponse.error("Failed to update profile", errors);
        }
    }

    @Operation(summary = "Delete current user profile", description = "Permanently delete the authenticated user's account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile deleted successfully"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/profile")
    @Override
    public ResponseEntity<Object> deleteCurrentUserProfile() {
        try {
            Long userId = getCurrentUserId();
            logger.info("Deleting profile for authenticated user ID: {}", userId);
            
            userEntityService.deleteUserProfile(userId);
            
            SuccessDto successDto = SuccessDto.builder().success(true).build();
            return buildResponse.success(successDto, "Profile deleted successfully");
        } catch (Exception e) {
            logger.error("Failed to delete current user profile", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("profile", e.getMessage());
            return buildResponse.error("Failed to delete profile", errors);
        }
    }

    @Operation(summary = "Upload profile picture", description = "Upload a new profile picture for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile picture uploaded successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid file or file too large"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @PostMapping(value = "/profile/picture", consumes = "multipart/form-data")
    @Override
    public ResponseEntity<Object> uploadProfilePicture(
            @Parameter(description = "Profile picture file (must be an image, max 10MB)", required = true)
            @RequestParam("file") MultipartFile file) {
        try {
            Long userId = getCurrentUserId();
            logger.info("Uploading profile picture for user ID: {}, filename: {}", userId, file.getOriginalFilename());
            
            // Upload file to Digital Ocean Spaces
            File uploadedFile = fileUploadService.uploadProfilePicture(file, userId);
            
            // Update user's profile picture ID
            UserProfileDto updatedProfile = userEntityService.updateProfilePicture(userId, Long.valueOf(uploadedFile.getId()));
            
            // Add file URL to response
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("profile", updatedProfile);
            responseData.put("fileUrl", fileUploadService.getFileUrl(uploadedFile.getId()));
            responseData.put("fileId", uploadedFile.getId());
            
            return buildResponse.success(responseData, "Profile picture uploaded successfully");
        } catch (Exception e) {
            logger.error("Failed to upload profile picture", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("profilePicture", e.getMessage());
            return buildResponse.error("Failed to upload profile picture", errors);
        }
    }

    @Operation(summary = "Remove profile picture", description = "Remove the authenticated user's profile picture")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile picture removed successfully"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @DeleteMapping("/profile/picture")
    @Override
    public ResponseEntity<Object> removeProfilePicture() {
        try {
            Long userId = getCurrentUserId();
            logger.info("Removing profile picture for user ID: {}", userId);
            
            userEntityService.removeProfilePicture(userId);
            
            SuccessDto successDto = SuccessDto.builder().success(true).build();
            return buildResponse.success(successDto, "Profile picture removed successfully");
        } catch (Exception e) {
            logger.error("Failed to remove profile picture", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("profilePicture", e.getMessage());
            return buildResponse.error("Failed to remove profile picture", errors);
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
        
        // The principal should be the user's email or ID
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