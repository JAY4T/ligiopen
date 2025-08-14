package com.jabulani.ligiopen.controller.player;

import com.jabulani.ligiopen.config.web.BuildResponse;
import com.jabulani.ligiopen.dto.player.*;
import com.jabulani.ligiopen.entity.player.Player;
import com.jabulani.ligiopen.entity.player.ClubMembership;
import com.jabulani.ligiopen.entity.player.PlayerTransfer;
import com.jabulani.ligiopen.service.player.PlayerService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/players")
@Tag(name = "Player Management", description = "APIs for player registration, profiles, and management")
@SecurityRequirement(name = "bearerAuth")
public class PlayerControllerImpl implements PlayerController {

    private static final Logger logger = LoggerFactory.getLogger(PlayerControllerImpl.class);

    private final PlayerService playerService;
    private final UserEntityService userEntityService;
    private final FileUploadService fileUploadService;
    private final PlayerMapper playerMapper;
    private final BuildResponse buildResponse;

    @Autowired
    public PlayerControllerImpl(PlayerService playerService, UserEntityService userEntityService,
                              FileUploadService fileUploadService, PlayerMapper playerMapper,
                              BuildResponse buildResponse) {
        this.playerService = playerService;
        this.userEntityService = userEntityService;
        this.fileUploadService = fileUploadService;
        this.playerMapper = playerMapper;
        this.buildResponse = buildResponse;
    }

    @Operation(summary = "Register new player", description = "Register a new player in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Player registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid registration data"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "409", description = "Player with email already exists")
    })
    @PostMapping("/registration")
    @Override
    public ResponseEntity<Object> registerPlayer(
            @Parameter(description = "Player registration data", required = true)
            @Valid @RequestBody PlayerRegistrationDto registrationDto) {
        try {
            logger.info("Registering new player: {} {}", registrationDto.getFirstName(), registrationDto.getLastName());

            Player player = playerService.registerPlayer(
                registrationDto.getFirstName(),
                registrationDto.getLastName(),
                registrationDto.getDateOfBirth(),
                registrationDto.getNationality(),
                registrationDto.getPrimaryPosition(),
                registrationDto.getEmail(),
                registrationDto.getPhoneNumber()
            );

            // Update with additional information if provided
            if (hasAdditionalInfo(registrationDto)) {
                player = playerService.updatePlayer(
                    player.getId(),
                    null, null, // Keep existing first/last name
                    registrationDto.getNickname(),
                    null, // Keep existing date of birth
                    null, // Keep existing nationality
                    registrationDto.getSecondNationality(),
                    registrationDto.getHeightCm(),
                    registrationDto.getWeightKg(),
                    registrationDto.getPreferredFoot(),
                    null, // Keep existing primary position
                    registrationDto.getSecondaryPositions(),
                    registrationDto.getBio(),
                    null, // Keep existing phone
                    null, // Keep existing email
                    registrationDto.getEmergencyContactName(),
                    registrationDto.getEmergencyContactPhone(),
                    registrationDto.getEmergencyContactRelationship(),
                    registrationDto.getIdNumber(),
                    registrationDto.getPassportNumber(),
                    registrationDto.getFkfRegistrationNumber(),
                    registrationDto.getMarketValue()
                );
            }

            PlayerDto playerDto = playerMapper.toPlayerDto(player);
            return buildResponse.success(playerDto, "Player registered successfully");

        } catch (Exception e) {
            logger.error("Failed to register player: {} {}", registrationDto.getFirstName(), registrationDto.getLastName(), e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("registration", e.getMessage());
            return buildResponse.error("Failed to register player", errors);
        }
    }

    @Operation(summary = "Get player by ID", description = "Get detailed player information by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Player not found")
    })
    @GetMapping("/{playerId}")
    @Override
    public ResponseEntity<Object> getPlayerById(
            @Parameter(description = "Player ID", required = true) @PathVariable Long playerId) {
        try {
            logger.info("Fetching player with ID: {}", playerId);

            Player player = playerService.getPlayerByIdOrThrow(playerId);
            PlayerDto playerDto = playerMapper.toPlayerDto(player);

            return buildResponse.success(playerDto, "Player retrieved successfully");

        } catch (Exception e) {
            logger.error("Failed to fetch player with ID: {}", playerId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("player", e.getMessage());
            return buildResponse.error("Failed to fetch player", errors);
        }
    }

    @Operation(summary = "Update player profile", description = "Update player profile information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid update data"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "404", description = "Player not found")
    })
    @PutMapping("/{playerId}")
    @Override
    public ResponseEntity<Object> updatePlayerProfile(
            @Parameter(description = "Player ID", required = true) @PathVariable Long playerId,
            @Parameter(description = "Player update data", required = true)
            @Valid @RequestBody PlayerUpdateDto updateDto) {
        try {
            logger.info("Updating player profile for ID: {}", playerId);

            Player player = playerService.updatePlayer(
                playerId,
                updateDto.getFirstName(),
                updateDto.getLastName(),
                updateDto.getNickname(),
                updateDto.getDateOfBirth(),
                updateDto.getNationality(),
                updateDto.getSecondNationality(),
                updateDto.getHeightCm(),
                updateDto.getWeightKg(),
                updateDto.getPreferredFoot(),
                updateDto.getPrimaryPosition(),
                updateDto.getSecondaryPositions(),
                updateDto.getBio(),
                updateDto.getPhoneNumber(),
                updateDto.getEmail(),
                updateDto.getEmergencyContactName(),
                updateDto.getEmergencyContactPhone(),
                updateDto.getEmergencyContactRelationship(),
                updateDto.getIdNumber(),
                updateDto.getPassportNumber(),
                updateDto.getFkfRegistrationNumber(),
                updateDto.getMarketValue()
            );

            PlayerDto playerDto = playerMapper.toPlayerDto(player);
            return buildResponse.success(playerDto, "Player updated successfully");

        } catch (Exception e) {
            logger.error("Failed to update player with ID: {}", playerId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("update", e.getMessage());
            return buildResponse.error("Failed to update player", errors);
        }
    }

    @Operation(summary = "Upload player main photo", description = "Upload main photo for player")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Photo uploaded successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid file"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "404", description = "Player not found")
    })
    @PostMapping(value = "/{playerId}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ResponseEntity<Object> uploadPlayerMainPhoto(
            @Parameter(description = "Player ID", required = true) @PathVariable Long playerId,
            @Parameter(description = "Photo file", required = true) @RequestParam("photo") MultipartFile photoFile) {
        try {
            logger.info("Uploading main photo for player ID: {}", playerId);

            if (photoFile.isEmpty()) {
                Map<String, Object> errors = new HashMap<>();
                errors.put("file", "Photo file is required");
                return buildResponse.error("Invalid file", errors);
            }

            String contentType = photoFile.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                Map<String, Object> errors = new HashMap<>();
                errors.put("file", "Only image files are allowed");
                return buildResponse.error("Invalid file type", errors);
            }

            Player player = playerService.updatePlayerMainPhoto(playerId, photoFile);
            PlayerDto playerDto = playerMapper.toPlayerDto(player);

            return buildResponse.success(playerDto, "Player photo uploaded successfully");

        } catch (Exception e) {
            logger.error("Failed to upload photo for player ID: {}", playerId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("upload", e.getMessage());
            return buildResponse.error("Failed to upload photo", errors);
        }
    }

    @Operation(summary = "Add player photo", description = "Add additional photo to player gallery")
    @PostMapping(value = "/{playerId}/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ResponseEntity<Object> addPlayerPhoto(
            @Parameter(description = "Player ID", required = true) @PathVariable Long playerId,
            @Parameter(description = "Photo file", required = true) @RequestParam("photo") MultipartFile photoFile) {
        try {
            logger.info("Adding photo for player ID: {}", playerId);

            if (photoFile.isEmpty()) {
                Map<String, Object> errors = new HashMap<>();
                errors.put("file", "Photo file is required");
                return buildResponse.error("Invalid file", errors);
            }

            Player player = playerService.addPlayerPhoto(playerId, photoFile);
            PlayerDto playerDto = playerMapper.toPlayerDto(player);

            return buildResponse.success(playerDto, "Player photo added successfully");

        } catch (Exception e) {
            logger.error("Failed to add photo for player ID: {}", playerId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("upload", e.getMessage());
            return buildResponse.error("Failed to add photo", errors);
        }
    }

    @Operation(summary = "Delete player main photo", description = "Delete player's main photo")
    @DeleteMapping("/{playerId}/photo")
    @Override
    public ResponseEntity<Object> deletePlayerMainPhoto(
            @Parameter(description = "Player ID", required = true) @PathVariable Long playerId) {
        try {
            logger.info("Deleting main photo for player ID: {}", playerId);

            Player player = playerService.deletePlayerMainPhoto(playerId);
            PlayerDto playerDto = playerMapper.toPlayerDto(player);

            return buildResponse.success(playerDto, "Player photo deleted successfully");

        } catch (Exception e) {
            logger.error("Failed to delete photo for player ID: {}", playerId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("delete", e.getMessage());
            return buildResponse.error("Failed to delete photo", errors);
        }
    }

    @Operation(summary = "Delete specific player photo", description = "Delete specific photo from player gallery")
    @DeleteMapping("/{playerId}/photos/{photoId}")
    @Override
    public ResponseEntity<Object> deletePlayerPhoto(
            @Parameter(description = "Player ID", required = true) @PathVariable Long playerId,
            @Parameter(description = "Photo ID", required = true) @PathVariable Long photoId) {
        try {
            logger.info("Deleting photo {} for player ID: {}", photoId, playerId);

            Player player = playerService.deletePlayerPhoto(playerId, photoId);
            PlayerDto playerDto = playerMapper.toPlayerDto(player);

            return buildResponse.success(playerDto, "Player photo deleted successfully");

        } catch (Exception e) {
            logger.error("Failed to delete photo {} for player ID: {}", photoId, playerId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("delete", e.getMessage());
            return buildResponse.error("Failed to delete photo", errors);
        }
    }

    @Operation(summary = "Search players by name", description = "Search players by name with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Players retrieved successfully")
    })
    @GetMapping("/search")
    @Override
    public ResponseEntity<Object> searchPlayersByName(
            @Parameter(description = "Search query") @RequestParam String query,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        try {
            logger.info("Searching players with query: '{}', page: {}, size: {}", query, page, size);

            PageRequest pageRequest = PageRequest.of(page, size);
            List<Player> players = playerService.searchPlayersByName(query, pageRequest);
            List<PlayerDto> playerDtos = players.stream()
                    .map(playerMapper::toBasicPlayerDto)
                    .toList();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("players", playerDtos);
            responseData.put("totalElements", playerDtos.size());
            responseData.put("currentPage", page);
            responseData.put("pageSize", size);

            return buildResponse.success(responseData, "Players retrieved successfully");

        } catch (Exception e) {
            logger.error("Failed to search players with query: '{}'", query, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("search", e.getMessage());
            return buildResponse.error("Failed to search players", errors);
        }
    }

    @Operation(summary = "Get players by position", description = "Get players by position with pagination")
    @GetMapping("/position/{position}")
    @Override
    public ResponseEntity<Object> getPlayersByPosition(
            @Parameter(description = "Player position", required = true) @PathVariable String position,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        try {
            logger.info("Fetching players by position: {}, page: {}, size: {}", position, page, size);

            Player.Position playerPosition = Player.Position.valueOf(position.toUpperCase());
            PageRequest pageRequest = PageRequest.of(page, size);
            List<Player> players = playerService.getPlayersByPosition(playerPosition, pageRequest);
            List<PlayerDto> playerDtos = players.stream()
                    .map(playerMapper::toBasicPlayerDto)
                    .toList();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("players", playerDtos);
            responseData.put("totalElements", playerDtos.size());
            responseData.put("currentPage", page);
            responseData.put("pageSize", size);
            responseData.put("position", position);

            return buildResponse.success(responseData, "Players retrieved successfully");

        } catch (Exception e) {
            logger.error("Failed to fetch players by position: {}", position, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("position", e.getMessage());
            return buildResponse.error("Failed to fetch players by position", errors);
        }
    }

    @Operation(summary = "Get players by nationality", description = "Get players by nationality with pagination")
    @GetMapping("/nationality/{nationality}")
    @Override
    public ResponseEntity<Object> getPlayersByNationality(
            @Parameter(description = "Player nationality", required = true) @PathVariable String nationality,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        try {
            logger.info("Fetching players by nationality: {}, page: {}, size: {}", nationality, page, size);

            PageRequest pageRequest = PageRequest.of(page, size);
            List<Player> players = playerService.getPlayersByNationality(nationality, pageRequest);
            List<PlayerDto> playerDtos = players.stream()
                    .map(playerMapper::toBasicPlayerDto)
                    .toList();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("players", playerDtos);
            responseData.put("totalElements", playerDtos.size());
            responseData.put("currentPage", page);
            responseData.put("pageSize", size);
            responseData.put("nationality", nationality);

            return buildResponse.success(responseData, "Players retrieved successfully");

        } catch (Exception e) {
            logger.error("Failed to fetch players by nationality: {}", nationality, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("nationality", e.getMessage());
            return buildResponse.error("Failed to fetch players by nationality", errors);
        }
    }

    @Operation(summary = "Get players by age range", description = "Get players within specified age range")
    @GetMapping("/age-range")
    @Override
    public ResponseEntity<Object> getPlayersByAgeRange(
            @Parameter(description = "Minimum age", required = true) @RequestParam int minAge,
            @Parameter(description = "Maximum age", required = true) @RequestParam int maxAge,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        try {
            logger.info("Fetching players by age range: {}-{}, page: {}, size: {}", minAge, maxAge, page, size);

            PageRequest pageRequest = PageRequest.of(page, size);
            List<Player> players = playerService.getPlayersByAgeRange(minAge, maxAge, pageRequest);
            List<PlayerDto> playerDtos = players.stream()
                    .map(playerMapper::toBasicPlayerDto)
                    .toList();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("players", playerDtos);
            responseData.put("totalElements", playerDtos.size());
            responseData.put("currentPage", page);
            responseData.put("pageSize", size);
            responseData.put("minAge", minAge);
            responseData.put("maxAge", maxAge);

            return buildResponse.success(responseData, "Players retrieved successfully");

        } catch (Exception e) {
            logger.error("Failed to fetch players by age range: {}-{}", minAge, maxAge, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("ageRange", e.getMessage());
            return buildResponse.error("Failed to fetch players by age range", errors);
        }
    }

    @Operation(summary = "Get free agents", description = "Get players without active club membership")
    @GetMapping("/free-agents")
    @Override
    public ResponseEntity<Object> getFreeAgents(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        try {
            logger.info("Fetching free agents, page: {}, size: {}", page, size);

            PageRequest pageRequest = PageRequest.of(page, size);
            List<Player> players = playerService.getFreeAgents(pageRequest);
            List<PlayerDto> playerDtos = players.stream()
                    .map(playerMapper::toBasicPlayerDto)
                    .toList();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("players", playerDtos);
            responseData.put("totalElements", playerDtos.size());
            responseData.put("currentPage", page);
            responseData.put("pageSize", size);

            return buildResponse.success(responseData, "Free agents retrieved successfully");

        } catch (Exception e) {
            logger.error("Failed to fetch free agents", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("freeAgents", e.getMessage());
            return buildResponse.error("Failed to fetch free agents", errors);
        }
    }

    @Operation(summary = "Get players by club", description = "Get all players in a specific club")
    @GetMapping("/club/{clubId}")
    @Override
    public ResponseEntity<Object> getPlayersByClub(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        try {
            logger.info("Fetching players by club ID: {}, page: {}, size: {}", clubId, page, size);

            PageRequest pageRequest = PageRequest.of(page, size);
            List<Player> players = playerService.getPlayersByClub(clubId, pageRequest);
            List<PlayerDto> playerDtos = players.stream()
                    .map(playerMapper::toBasicPlayerDto)
                    .toList();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("players", playerDtos);
            responseData.put("totalElements", playerDtos.size());
            responseData.put("currentPage", page);
            responseData.put("pageSize", size);
            responseData.put("clubId", clubId);

            return buildResponse.success(responseData, "Club players retrieved successfully");

        } catch (Exception e) {
            logger.error("Failed to fetch players by club ID: {}", clubId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("clubPlayers", e.getMessage());
            return buildResponse.error("Failed to fetch club players", errors);
        }
    }

    @Operation(summary = "Get all active players", description = "Get all active players with pagination")
    @GetMapping("/active")
    @Override
    public ResponseEntity<Object> getAllActivePlayers(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        try {
            logger.info("Fetching all active players, page: {}, size: {}", page, size);

            PageRequest pageRequest = PageRequest.of(page, size);
            List<Player> players = playerService.getAllActivePlayers(pageRequest);
            List<PlayerDto> playerDtos = players.stream()
                    .map(playerMapper::toBasicPlayerDto)
                    .toList();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("players", playerDtos);
            responseData.put("totalElements", playerDtos.size());
            responseData.put("currentPage", page);
            responseData.put("pageSize", size);

            return buildResponse.success(responseData, "Active players retrieved successfully");

        } catch (Exception e) {
            logger.error("Failed to fetch active players", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("activePlayers", e.getMessage());
            return buildResponse.error("Failed to fetch active players", errors);
        }
    }

    @Operation(summary = "Get current club membership", description = "Get player's current club membership")
    @GetMapping("/{playerId}/current-club")
    @Override
    public ResponseEntity<Object> getCurrentClubMembership(
            @Parameter(description = "Player ID", required = true) @PathVariable Long playerId) {
        try {
            logger.info("Getting current club membership for player ID: {}", playerId);

            Optional<ClubMembership> membershipOpt = playerService.getCurrentClubMembership(playerId);
            ClubMembership membership = membershipOpt.orElse(null);
            
            if (membership != null) {
                ClubMembershipDto membershipDto = playerMapper.toClubMembershipDto(membership);
                return buildResponse.success(membershipDto, "Current club membership retrieved successfully");
            } else {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("playerId", playerId);
                responseData.put("hasCurrentClub", false);
                responseData.put("status", "Free Agent");
                return buildResponse.success(responseData, "Player is currently a free agent");
            }

        } catch (Exception e) {
            logger.error("Failed to get current club membership for player ID: {}", playerId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("membership", e.getMessage());
            return buildResponse.error("Failed to get current club membership", errors);
        }
    }

    @Operation(summary = "Get player club history", description = "Get player's complete club membership history")
    @GetMapping("/{playerId}/club-history")
    @Override
    public ResponseEntity<Object> getPlayerClubHistory(
            @Parameter(description = "Player ID", required = true) @PathVariable Long playerId) {
        try {
            logger.info("Getting club history for player ID: {}", playerId);

            List<ClubMembership> memberships = playerService.getPlayerClubHistory(playerId);
            List<ClubMembershipDto> membershipDtos = memberships.stream()
                    .map(playerMapper::toClubMembershipDto)
                    .toList();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("playerId", playerId);
            responseData.put("memberships", membershipDtos);
            responseData.put("totalClubs", membershipDtos.size());

            return buildResponse.success(responseData, "Player club history retrieved successfully");

        } catch (Exception e) {
            logger.error("Failed to get club history for player ID: {}", playerId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("history", e.getMessage());
            return buildResponse.error("Failed to get club history", errors);
        }
    }

    @Operation(summary = "Get player statistics", description = "Get comprehensive player statistics")
    @GetMapping("/{playerId}/statistics")
    @Override
    public ResponseEntity<Object> getPlayerStatistics(
            @Parameter(description = "Player ID", required = true) @PathVariable Long playerId) {
        try {
            logger.info("Getting statistics for player ID: {}", playerId);

            Map<String, Object> statistics = playerService.getPlayerStatistics(playerId);
            return buildResponse.success(statistics, "Player statistics retrieved successfully");

        } catch (Exception e) {
            logger.error("Failed to get statistics for player ID: {}", playerId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("statistics", e.getMessage());
            return buildResponse.error("Failed to get player statistics", errors);
        }
    }

    @Operation(summary = "Get player transfer history", description = "Get player's complete transfer history")
    @GetMapping("/{playerId}/transfers")
    @Override
    public ResponseEntity<Object> getPlayerTransferHistory(
            @Parameter(description = "Player ID", required = true) @PathVariable Long playerId) {
        try {
            logger.info("Getting transfer history for player ID: {}", playerId);

            List<PlayerTransfer> transfers = playerService.getPlayerTransferHistory(playerId);
            List<PlayerTransferDto> transferDtos = transfers.stream()
                    .map(playerMapper::toPlayerTransferDto)
                    .toList();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("playerId", playerId);
            responseData.put("transfers", transferDtos);
            responseData.put("totalTransfers", transferDtos.size());

            return buildResponse.success(responseData, "Player transfer history retrieved successfully");

        } catch (Exception e) {
            logger.error("Failed to get transfer history for player ID: {}", playerId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("transfers", e.getMessage());
            return buildResponse.error("Failed to get transfer history", errors);
        }
    }

    @Operation(summary = "Check transfer availability", description = "Check if player is available for transfer")
    @GetMapping("/{playerId}/transfer-availability")
    @Override
    public ResponseEntity<Object> checkPlayerTransferAvailability(
            @Parameter(description = "Player ID", required = true) @PathVariable Long playerId) {
        try {
            logger.info("Checking transfer availability for player ID: {}", playerId);

            boolean isAvailable = playerService.isPlayerAvailableForTransfer(playerId);
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("playerId", playerId);
            responseData.put("availableForTransfer", isAvailable);
            responseData.put("status", isAvailable ? "Available" : "Not Available");

            return buildResponse.success(responseData, "Transfer availability checked successfully");

        } catch (Exception e) {
            logger.error("Failed to check transfer availability for player ID: {}", playerId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("availability", e.getMessage());
            return buildResponse.error("Failed to check transfer availability", errors);
        }
    }

    @Operation(summary = "Deactivate player", description = "Deactivate player account")
    @PostMapping("/{playerId}/deactivate")
    @Override
    public ResponseEntity<Object> deactivatePlayer(
            @Parameter(description = "Player ID", required = true) @PathVariable Long playerId) {
        try {
            logger.info("Deactivating player ID: {}", playerId);

            Player player = playerService.deactivatePlayer(playerId);
            PlayerDto playerDto = playerMapper.toPlayerDto(player);

            return buildResponse.success(playerDto, "Player deactivated successfully");

        } catch (Exception e) {
            logger.error("Failed to deactivate player ID: {}", playerId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("deactivate", e.getMessage());
            return buildResponse.error("Failed to deactivate player", errors);
        }
    }

    @Operation(summary = "Reactivate player", description = "Reactivate player account")
    @PostMapping("/{playerId}/reactivate")
    @Override
    public ResponseEntity<Object> reactivatePlayer(
            @Parameter(description = "Player ID", required = true) @PathVariable Long playerId) {
        try {
            logger.info("Reactivating player ID: {}", playerId);

            Player player = playerService.reactivatePlayer(playerId);
            PlayerDto playerDto = playerMapper.toPlayerDto(player);

            return buildResponse.success(playerDto, "Player reactivated successfully");

        } catch (Exception e) {
            logger.error("Failed to reactivate player ID: {}", playerId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("reactivate", e.getMessage());
            return buildResponse.error("Failed to reactivate player", errors);
        }
    }

    @Operation(summary = "Delete player", description = "Delete player account (soft delete)")
    @DeleteMapping("/{playerId}")
    @Override
    public ResponseEntity<Object> deletePlayer(
            @Parameter(description = "Player ID", required = true) @PathVariable Long playerId) {
        try {
            logger.info("Deleting player ID: {}", playerId);

            playerService.deletePlayer(playerId);
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("playerId", playerId);
            responseData.put("deleted", true);

            return buildResponse.success(responseData, "Player deleted successfully");

        } catch (Exception e) {
            logger.error("Failed to delete player ID: {}", playerId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("delete", e.getMessage());
            return buildResponse.error("Failed to delete player", errors);
        }
    }

    @Operation(summary = "Get player by email", description = "Find player by email address")
    @GetMapping("/email/{email}")
    @Override
    public ResponseEntity<Object> getPlayerByEmail(
            @Parameter(description = "Player email", required = true) @PathVariable String email) {
        try {
            logger.info("Finding player by email: {}", email);

            Optional<Player> playerOpt = playerService.getPlayerByEmail(email);
            if (playerOpt.isEmpty()) {
                Map<String, Object> errors = new HashMap<>();
                errors.put("email", "Player not found with email: " + email);
                return buildResponse.error("Player not found", errors);
            }
            Player player = playerOpt.get();
            PlayerDto playerDto = playerMapper.toPlayerDto(player);
            return buildResponse.success(playerDto, "Player found successfully");

        } catch (Exception e) {
            logger.error("Failed to find player by email: {}", email, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("email", e.getMessage());
            return buildResponse.error("Failed to find player by email", errors);
        }
    }

    @Operation(summary = "Get player by FKF registration", description = "Find player by FKF registration number")
    @GetMapping("/fkf-registration/{registrationNumber}")
    @Override
    public ResponseEntity<Object> getPlayerByFkfRegistrationNumber(
            @Parameter(description = "FKF registration number", required = true) @PathVariable String registrationNumber) {
        try {
            logger.info("Finding player by FKF registration number: {}", registrationNumber);

            Optional<Player> playerOpt = playerService.getPlayerByFkfRegistrationNumber(registrationNumber);
            if (playerOpt.isEmpty()) {
                Map<String, Object> errors = new HashMap<>();
                errors.put("registrationNumber", "Player not found with FKF registration number: " + registrationNumber);
                return buildResponse.error("Player not found", errors);
            }
            Player player = playerOpt.get();
            PlayerDto playerDto = playerMapper.toPlayerDto(player);
            return buildResponse.success(playerDto, "Player found successfully");

        } catch (Exception e) {
            logger.error("Failed to find player by FKF registration number: {}", registrationNumber, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("registrationNumber", e.getMessage());
            return buildResponse.error("Failed to find player by FKF registration number", errors);
        }
    }

    @Operation(summary = "Check email availability", description = "Check if email is available for registration")
    @GetMapping("/check-email")
    @Override
    public ResponseEntity<Object> checkEmailAvailability(
            @Parameter(description = "Email to check", required = true) @RequestParam String email) {
        try {
            logger.info("Checking email availability: {}", email);

            boolean isAvailable = playerService.isEmailAvailable(email);
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("email", email);
            responseData.put("available", isAvailable);

            return buildResponse.success(responseData, "Email availability checked successfully");

        } catch (Exception e) {
            logger.error("Failed to check email availability: {}", email, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("email", e.getMessage());
            return buildResponse.error("Failed to check email availability", errors);
        }
    }

    @Operation(summary = "Check FKF registration availability", description = "Check if FKF registration number is available")
    @GetMapping("/check-fkf-registration")
    @Override
    public ResponseEntity<Object> checkFkfRegistrationAvailability(
            @Parameter(description = "FKF registration number to check", required = true) @RequestParam String registrationNumber) {
        try {
            logger.info("Checking FKF registration availability: {}", registrationNumber);

            boolean isAvailable = playerService.isFkfRegistrationNumberAvailable(registrationNumber);
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("registrationNumber", registrationNumber);
            responseData.put("available", isAvailable);

            return buildResponse.success(responseData, "FKF registration availability checked successfully");

        } catch (Exception e) {
            logger.error("Failed to check FKF registration availability: {}", registrationNumber, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("registrationNumber", e.getMessage());
            return buildResponse.error("Failed to check FKF registration availability", errors);
        }
    }

    @Operation(summary = "Get recently registered players", description = "Get recently registered players")
    @GetMapping("/recent")
    @Override
    public ResponseEntity<Object> getRecentlyRegisteredPlayers(
            @Parameter(description = "Limit") @RequestParam(defaultValue = "10") int limit) {
        try {
            logger.info("Fetching recently registered players (limit: {})", limit);

            List<Player> players = playerService.getRecentlyRegisteredPlayers(limit);
            List<PlayerDto> playerDtos = players.stream()
                    .map(playerMapper::toBasicPlayerDto)
                    .toList();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("players", playerDtos);
            responseData.put("limit", limit);

            return buildResponse.success(responseData, "Recently registered players retrieved successfully");

        } catch (Exception e) {
            logger.error("Failed to fetch recently registered players", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("recent", e.getMessage());
            return buildResponse.error("Failed to fetch recently registered players", errors);
        }
    }

    @Operation(summary = "Get top valued players", description = "Get players with highest market value")
    @GetMapping("/top-valued")
    @Override
    public ResponseEntity<Object> getTopValuedPlayers(
            @Parameter(description = "Limit") @RequestParam(defaultValue = "10") int limit) {
        try {
            logger.info("Fetching top valued players (limit: {})", limit);

            List<Player> players = playerService.getTopValuedPlayers(limit);
            List<PlayerDto> playerDtos = players.stream()
                    .map(playerMapper::toBasicPlayerDto)
                    .toList();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("players", playerDtos);
            responseData.put("limit", limit);

            return buildResponse.success(responseData, "Top valued players retrieved successfully");

        } catch (Exception e) {
            logger.error("Failed to fetch top valued players", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("topValued", e.getMessage());
            return buildResponse.error("Failed to fetch top valued players", errors);
        }
    }

    @Operation(summary = "Get players by preferred foot", description = "Get players by preferred foot")
    @GetMapping("/preferred-foot/{foot}")
    @Override
    public ResponseEntity<Object> getPlayersByPreferredFoot(
            @Parameter(description = "Preferred foot (LEFT, RIGHT, BOTH)", required = true) @PathVariable String foot,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        try {
            logger.info("Fetching players by preferred foot: {}, page: {}, size: {}", foot, page, size);

            PageRequest pageRequest = PageRequest.of(page, size);
            // For now, return empty list until service method is implemented
            List<PlayerDto> playerDtos = List.of();
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("players", playerDtos);
            responseData.put("totalElements", playerDtos.size());
            responseData.put("currentPage", page);
            responseData.put("pageSize", size);
            responseData.put("preferredFoot", foot);
            
            return buildResponse.success(responseData, "Players retrieved successfully");

        } catch (Exception e) {
            logger.error("Failed to fetch players by preferred foot: {}", foot, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("preferredFoot", e.getMessage());
            return buildResponse.error("Failed to fetch players by preferred foot", errors);
        }
    }

    @Operation(summary = "Get players by height range", description = "Get players within height range")
    @GetMapping("/height-range")
    @Override
    public ResponseEntity<Object> getPlayersByHeightRange(
            @Parameter(description = "Minimum height (cm)", required = true) @RequestParam Double minHeight,
            @Parameter(description = "Maximum height (cm)", required = true) @RequestParam Double maxHeight,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        try {
            logger.info("Fetching players by height range: {}-{}cm, page: {}, size: {}", minHeight, maxHeight, page, size);

            PageRequest pageRequest = PageRequest.of(page, size);
            // For now, return empty list until service method is implemented  
            List<PlayerDto> playerDtos = List.of();
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("players", playerDtos);
            responseData.put("totalElements", playerDtos.size());
            responseData.put("currentPage", page);
            responseData.put("pageSize", size);
            responseData.put("minHeight", minHeight);
            responseData.put("maxHeight", maxHeight);
            
            return buildResponse.success(responseData, "Players retrieved successfully");

        } catch (Exception e) {
            logger.error("Failed to fetch players by height range: {}-{}", minHeight, maxHeight, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("heightRange", e.getMessage());
            return buildResponse.error("Failed to fetch players by height range", errors);
        }
    }

    // Utility methods

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

    private boolean hasAdditionalInfo(PlayerRegistrationDto dto) {
        return dto.getNickname() != null || 
               dto.getSecondNationality() != null ||
               dto.getHeightCm() != null ||
               dto.getWeightKg() != null ||
               dto.getPreferredFoot() != null ||
               dto.getSecondaryPositions() != null ||
               dto.getBio() != null ||
               dto.getEmergencyContactName() != null ||
               dto.getEmergencyContactPhone() != null ||
               dto.getEmergencyContactRelationship() != null ||
               dto.getIdNumber() != null ||
               dto.getPassportNumber() != null ||
               dto.getFkfRegistrationNumber() != null ||
               dto.getMarketValue() != null;
    }
}