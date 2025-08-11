package com.jabulani.ligiopen.controller.club;

import com.jabulani.ligiopen.config.web.BuildResponse;
import com.jabulani.ligiopen.dto.club.ClubDto;
import com.jabulani.ligiopen.dto.club.ClubMapper;
import com.jabulani.ligiopen.dto.user.UserDto;
import com.jabulani.ligiopen.mapper.UserMapper;
import com.jabulani.ligiopen.entity.club.Club;
import com.jabulani.ligiopen.entity.club.FavoritedClub;
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
@RequestMapping("/api/v1/clubs")
@Tag(name = "Club User Relationships", description = "APIs for managing user-club relationships (favorites, ownership)")
@SecurityRequirement(name = "bearerAuth")
public class ClubRelationshipControllerImpl implements ClubRelationshipController {

    private static final Logger logger = LoggerFactory.getLogger(ClubRelationshipControllerImpl.class);

    private final ClubService clubService;
    private final UserEntityService userEntityService;
    private final ClubMapper clubMapper;
    private final UserMapper userMapper;
    private final BuildResponse buildResponse;

    @Autowired
    public ClubRelationshipControllerImpl(ClubService clubService, UserEntityService userEntityService,
                                        ClubMapper clubMapper, UserMapper userMapper, BuildResponse buildResponse) {
        this.clubService = clubService;
        this.userEntityService = userEntityService;
        this.clubMapper = clubMapper;
        this.userMapper = userMapper;
        this.buildResponse = buildResponse;
    }

    @Operation(summary = "Add club to favorites", 
               description = "Add a club to the current user's favorites")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Club added to favorites successfully"),
        @ApiResponse(responseCode = "400", description = "Club already in favorites"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "404", description = "Club not found")
    })
    @PostMapping("/{clubId}/favorite")
    @Override
    public ResponseEntity<Object> addClubToFavorites(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId) {
        try {
            Long userId = getCurrentUserId();
            logger.info("Adding club ID: {} to favorites for user ID: {}", clubId, userId);

            // This would require a service method for favorites management
            Map<String, Object> errors = new HashMap<>();
            errors.put("feature", "Club favorites feature requires additional implementation");
            return buildResponse.error("Feature not fully implemented", errors);

        } catch (Exception e) {
            logger.error("Failed to add club ID: {} to favorites for user", clubId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("favorite", e.getMessage());
            return buildResponse.error("Failed to add club to favorites", errors);
        }
    }

    @Operation(summary = "Remove club from favorites", 
               description = "Remove a club from the current user's favorites")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Club removed from favorites successfully"),
        @ApiResponse(responseCode = "400", description = "Club not in favorites"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "404", description = "Club not found")
    })
    @DeleteMapping("/{clubId}/favorite")
    @Override
    public ResponseEntity<Object> removeClubFromFavorites(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId) {
        try {
            Long userId = getCurrentUserId();
            logger.info("Removing club ID: {} from favorites for user ID: {}", clubId, userId);

            Map<String, Object> errors = new HashMap<>();
            errors.put("feature", "Club favorites feature requires additional implementation");
            return buildResponse.error("Feature not fully implemented", errors);

        } catch (Exception e) {
            logger.error("Failed to remove club ID: {} from favorites for user", clubId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("favorite", e.getMessage());
            return buildResponse.error("Failed to remove club from favorites", errors);
        }
    }

    @Operation(summary = "Check if club is favorited", 
               description = "Check if the current user has favorited a club")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Favorite status retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "404", description = "Club not found")
    })
    @GetMapping("/{clubId}/favorite/status")
    @Override
    public ResponseEntity<Object> isClubFavorited(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId) {
        try {
            Long userId = getCurrentUserId();
            logger.info("Checking favorite status for club ID: {} and user ID: {}", clubId, userId);

            // For now, return false as placeholder
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("clubId", clubId);
            responseData.put("userId", userId);
            responseData.put("isFavorited", false); // Would check actual relationship
            
            return buildResponse.success(responseData, "Favorite status retrieved");

        } catch (Exception e) {
            logger.error("Failed to check favorite status for club ID: {}", clubId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("status", e.getMessage());
            return buildResponse.error("Failed to check favorite status", errors);
        }
    }

    @Operation(summary = "Get user's favorite clubs", 
               description = "Get all clubs favorited by the current user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Favorite clubs retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @GetMapping("/favorites/my")
    @Override
    public ResponseEntity<Object> getUserFavoriteClubs(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        try {
            Long userId = getCurrentUserId();
            logger.info("Getting favorite clubs for user ID: {}, page: {}, size: {}", userId, page, size);

            List<Club> favoriteClubs = clubService.getClubsFavoritedByUser(userId);
            List<ClubDto> clubDtos = favoriteClubs.stream()
                    .map(clubMapper::toBasicClubDto)
                    .toList();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("clubs", clubDtos);
            responseData.put("totalElements", clubDtos.size());
            responseData.put("currentPage", page);
            responseData.put("pageSize", size);

            return buildResponse.success(responseData, "Favorite clubs retrieved successfully");

        } catch (Exception e) {
            logger.error("Failed to get favorite clubs for user", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("favorites", e.getMessage());
            return buildResponse.error("Failed to get favorite clubs", errors);
        }
    }

    @Operation(summary = "Get club favorites", 
               description = "Get users who have favorited a specific club")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Club favorites retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Club not found")
    })
    @GetMapping("/{clubId}/favorites")
    @Override
    public ResponseEntity<Object> getClubFavorites(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        try {
            logger.info("Getting favorites for club ID: {}, page: {}, size: {}", clubId, page, size);

            Club club = clubService.getClubById(clubId);
            
            // Get users who favorited this club
            List<UserDto> favoritedByUsers = club.getFavoritedByUsers().stream()
                    .map(FavoritedClub::getUser)
                    .map(userMapper::toUserDto)
                    .toList();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("users", favoritedByUsers);
            responseData.put("totalElements", favoritedByUsers.size());
            responseData.put("currentPage", page);
            responseData.put("pageSize", size);
            responseData.put("clubId", clubId);
            responseData.put("clubName", club.getName());

            return buildResponse.success(responseData, "Club favorites retrieved successfully");

        } catch (Exception e) {
            logger.error("Failed to get favorites for club ID: {}", clubId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("favorites", e.getMessage());
            return buildResponse.error("Failed to get club favorites", errors);
        }
    }

    @Operation(summary = "Get club favorites count", 
               description = "Get the number of users who have favorited a club")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Favorites count retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Club not found")
    })
    @GetMapping("/{clubId}/favorites/count")
    @Override
    public ResponseEntity<Object> getClubFavoritesCount(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId) {
        try {
            logger.info("Getting favorites count for club ID: {}", clubId);

            Club club = clubService.getClubById(clubId);
            int favoritesCount = club.getFavoritedByUsers() != null ? club.getFavoritedByUsers().size() : 0;

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("clubId", clubId);
            responseData.put("clubName", club.getName());
            responseData.put("favoritesCount", favoritesCount);

            return buildResponse.success(responseData, "Favorites count retrieved successfully");

        } catch (Exception e) {
            logger.error("Failed to get favorites count for club ID: {}", clubId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("count", e.getMessage());
            return buildResponse.error("Failed to get favorites count", errors);
        }
    }

    @Operation(summary = "Get user's owned clubs", 
               description = "Get all clubs owned by the current user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Owned clubs retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @GetMapping("/relationships/owned")
    @Override
    public ResponseEntity<Object> getUserOwnedClubs() {
        try {
            Long userId = getCurrentUserId();
            logger.info("Getting owned clubs for user ID: {}", userId);

            List<Club> ownedClubs = clubService.getClubsOwnedByUser(userId);
            List<ClubDto> clubDtos = ownedClubs.stream()
                    .map(clubMapper::toClubDto)
                    .toList();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("clubs", clubDtos);
            responseData.put("totalElements", clubDtos.size());

            return buildResponse.success(responseData, "Owned clubs retrieved successfully");

        } catch (Exception e) {
            logger.error("Failed to get owned clubs for user", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("owned", e.getMessage());
            return buildResponse.error("Failed to get owned clubs", errors);
        }
    }

    @Operation(summary = "Get user's managed clubs", 
               description = "Get all clubs managed by the current user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Managed clubs retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @GetMapping("/relationships/managed")
    @Override
    public ResponseEntity<Object> getUserManagedClubs() {
        try {
            Long userId = getCurrentUserId();
            logger.info("Getting managed clubs for user ID: {}", userId);

            List<Club> managedClubs = clubService.getClubsManagedByUser(userId);
            List<ClubDto> clubDtos = managedClubs.stream()
                    .map(clubMapper::toClubDto)
                    .toList();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("clubs", clubDtos);
            responseData.put("totalElements", clubDtos.size());

            return buildResponse.success(responseData, "Managed clubs retrieved successfully");

        } catch (Exception e) {
            logger.error("Failed to get managed clubs for user", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("managed", e.getMessage());
            return buildResponse.error("Failed to get managed clubs", errors);
        }
    }

    @Operation(summary = "Get user's club relationships", 
               description = "Get summary of user's relationships with clubs")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Relationships retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @GetMapping("/relationships/summary")
    @Override
    public ResponseEntity<Object> getUserClubRelationships() {
        try {
            Long userId = getCurrentUserId();
            logger.info("Getting club relationships summary for user ID: {}", userId);

            List<Club> ownedClubs = clubService.getClubsOwnedByUser(userId);
            List<Club> managedClubs = clubService.getClubsManagedByUser(userId);
            List<Club> favoriteClubs = clubService.getClubsFavoritedByUser(userId);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("ownedClubs", ownedClubs.stream().map(clubMapper::toBasicClubDto).toList());
            responseData.put("managedClubs", managedClubs.stream().map(clubMapper::toBasicClubDto).toList());
            responseData.put("favoriteClubs", favoriteClubs.stream().map(clubMapper::toBasicClubDto).toList());
            responseData.put("ownedCount", ownedClubs.size());
            responseData.put("managedCount", managedClubs.size());
            responseData.put("favoritesCount", favoriteClubs.size());

            return buildResponse.success(responseData, "Club relationships retrieved successfully");

        } catch (Exception e) {
            logger.error("Failed to get club relationships for user", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("relationships", e.getMessage());
            return buildResponse.error("Failed to get club relationships", errors);
        }
    }

    @Operation(summary = "Get popular clubs", 
               description = "Get clubs with most favorites")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Popular clubs retrieved successfully")
    })
    @GetMapping("/popular")
    @Override
    public ResponseEntity<Object> getPopularClubs(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        try {
            logger.info("Getting popular clubs, page: {}, size: {}", page, size);

            // For now, just return all active clubs as placeholder
            // In full implementation, this would be sorted by favorites count
            List<Club> popularClubs = clubService.getAllActiveClubs();
            List<ClubDto> clubDtos = popularClubs.stream()
                    .limit(size)
                    .skip((long) page * size)
                    .map(clubMapper::toBasicClubDto)
                    .toList();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("clubs", clubDtos);
            responseData.put("totalElements", clubDtos.size());
            responseData.put("currentPage", page);
            responseData.put("pageSize", size);

            return buildResponse.success(responseData, "Popular clubs retrieved successfully");

        } catch (Exception e) {
            logger.error("Failed to get popular clubs", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("popular", e.getMessage());
            return buildResponse.error("Failed to get popular clubs", errors);
        }
    }

    @Operation(summary = "Get recommended clubs", 
               description = "Get personalized club recommendations for the current user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recommended clubs retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @GetMapping("/recommendations")
    @Override
    public ResponseEntity<Object> getRecommendedClubs(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        try {
            Long userId = getCurrentUserId();
            logger.info("Getting recommended clubs for user ID: {}, page: {}, size: {}", userId, page, size);

            // For now, return recently created clubs as placeholder recommendations
            // In full implementation, this would use recommendation algorithms
            List<Club> recommendedClubs = clubService.getRecentlyCreatedClubs(size);
            List<ClubDto> clubDtos = recommendedClubs.stream()
                    .map(clubMapper::toBasicClubDto)
                    .toList();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("clubs", clubDtos);
            responseData.put("totalElements", clubDtos.size());
            responseData.put("currentPage", page);
            responseData.put("pageSize", size);
            responseData.put("recommendationType", "recently_created"); // Would indicate algorithm used

            return buildResponse.success(responseData, "Recommended clubs retrieved successfully");

        } catch (Exception e) {
            logger.error("Failed to get recommended clubs for user", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("recommendations", e.getMessage());
            return buildResponse.error("Failed to get recommended clubs", errors);
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