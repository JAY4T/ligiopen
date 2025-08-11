package com.jabulani.ligiopen.controller.club;

import com.jabulani.ligiopen.config.web.BuildResponse;
import com.jabulani.ligiopen.dto.club.ClubDto;
import com.jabulani.ligiopen.dto.club.ClubMapper;
import com.jabulani.ligiopen.dto.club.ClubUpdateDto;
import com.jabulani.ligiopen.entity.club.Club;
import com.jabulani.ligiopen.service.club.ClubService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/clubs")
@Tag(name = "Club Profile Management", description = "APIs for club profile CRUD operations")
@SecurityRequirement(name = "bearerAuth")
public class ClubProfileControllerImpl implements ClubProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ClubProfileControllerImpl.class);

    private final ClubService clubService;
    private final UserEntityService userEntityService;
    private final FileUploadService fileUploadService;
    private final ClubMapper clubMapper;
    private final BuildResponse buildResponse;

    @Autowired
    public ClubProfileControllerImpl(ClubService clubService, UserEntityService userEntityService,
                                   FileUploadService fileUploadService, ClubMapper clubMapper,
                                   BuildResponse buildResponse) {
        this.clubService = clubService;
        this.userEntityService = userEntityService;
        this.fileUploadService = fileUploadService;
        this.clubMapper = clubMapper;
        this.buildResponse = buildResponse;
    }

    @Operation(summary = "Get club by ID", description = "Get detailed club information by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Club retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Club not found")
    })
    @GetMapping("/{clubId}")
    @Override
    public ResponseEntity<Object> getClubById(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId) {
        try {
            logger.info("Fetching club with ID: {}", clubId);
            
            Club club = clubService.getClubById(clubId);
            ClubDto clubDto = clubMapper.toClubDto(club);
            
            return buildResponse.success(clubDto, "Club retrieved successfully");
            
        } catch (Exception e) {
            logger.error("Failed to fetch club with ID: {}", clubId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("club", e.getMessage());
            return buildResponse.error("Failed to fetch club", errors);
        }
    }

    @Operation(summary = "Update club profile", 
               description = "Update club profile information (owner/manager only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Club updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid update data"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "403", description = "User not authorized (must be club owner/manager)"),
        @ApiResponse(responseCode = "404", description = "Club not found")
    })
    @PutMapping("/{clubId}")
    @Override
    public ResponseEntity<Object> updateClubProfile(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId,
            @Parameter(description = "Club update data", required = true)
            @Valid @RequestBody ClubUpdateDto updateDto) {
        try {
            Long userId = getCurrentUserId();
            logger.info("Updating club ID: {} by user ID: {}", clubId, userId);
            
            Club club = clubService.updateClub(clubId, userId, updateDto.getName(), 
                updateDto.getShortName(), updateDto.getAbbreviation(), updateDto.getFounded(),
                updateDto.getDescription(), updateDto.getColors(), updateDto.getContactEmail(),
                updateDto.getContactPhone(), updateDto.getWebsiteUrl(), updateDto.getSocialMediaLinks(),
                updateDto.getCountyId(), updateDto.getHomeStadiumId(), updateDto.getClubLevel());
                
            ClubDto clubDto = clubMapper.toClubDto(club);
            
            return buildResponse.success(clubDto, "Club updated successfully");
            
        } catch (Exception e) {
            logger.error("Failed to update club ID: {}", clubId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("update", e.getMessage());
            return buildResponse.error("Failed to update club", errors);
        }
    }

    @Operation(summary = "Upload club logo", 
               description = "Upload club logo (owner/manager only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logo uploaded successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid file or file too large"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "403", description = "User not authorized (must be club owner/manager)")
    })
    @PostMapping(value = "/{clubId}/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ResponseEntity<Object> uploadClubLogo(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId,
            @Parameter(description = "Logo file", required = true) 
            @RequestParam("logo") MultipartFile logoFile) {
        try {
            Long userId = getCurrentUserId();
            logger.info("Uploading logo for club ID: {} by user ID: {}", clubId, userId);
            
            if (logoFile.isEmpty()) {
                Map<String, Object> errors = new HashMap<>();
                errors.put("file", "Logo file is required");
                return buildResponse.error("Invalid file", errors);
            }
            
            // Validate file type (images only)
            String contentType = logoFile.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                Map<String, Object> errors = new HashMap<>();
                errors.put("file", "Only image files are allowed");
                return buildResponse.error("Invalid file type", errors);
            }
            
            Club club = clubService.updateClubLogo(clubId, userId, logoFile);
            ClubDto clubDto = clubMapper.toClubDto(club);
            
            return buildResponse.success(clubDto, "Club logo uploaded successfully");
            
        } catch (Exception e) {
            logger.error("Failed to upload logo for club ID: {}", clubId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("upload", e.getMessage());
            return buildResponse.error("Failed to upload logo", errors);
        }
    }

    @Operation(summary = "Upload club main photo", 
               description = "Upload club main photo (owner/manager only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Photo uploaded successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid file or file too large"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "403", description = "User not authorized (must be club owner/manager)")
    })
    @PostMapping(value = "/{clubId}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ResponseEntity<Object> uploadClubMainPhoto(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId,
            @Parameter(description = "Photo file", required = true) 
            @RequestParam("photo") MultipartFile photoFile) {
        try {
            Long userId = getCurrentUserId();
            logger.info("Uploading main photo for club ID: {} by user ID: {}", clubId, userId);
            
            if (photoFile.isEmpty()) {
                Map<String, Object> errors = new HashMap<>();
                errors.put("file", "Photo file is required");
                return buildResponse.error("Invalid file", errors);
            }
            
            // Validate file type (images only)
            String contentType = photoFile.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                Map<String, Object> errors = new HashMap<>();
                errors.put("file", "Only image files are allowed");
                return buildResponse.error("Invalid file type", errors);
            }
            
            Club club = clubService.updateClubMainPhoto(clubId, userId, photoFile);
            ClubDto clubDto = clubMapper.toClubDto(club);
            
            return buildResponse.success(clubDto, "Club main photo uploaded successfully");
            
        } catch (Exception e) {
            logger.error("Failed to upload main photo for club ID: {}", clubId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("upload", e.getMessage());
            return buildResponse.error("Failed to upload photo", errors);
        }
    }

    @Operation(summary = "Delete club logo", 
               description = "Delete club logo (owner/manager only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logo deleted successfully"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "403", description = "User not authorized (must be club owner/manager)")
    })
    @DeleteMapping("/{clubId}/logo")
    @Override
    public ResponseEntity<Object> deleteClubLogo(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId) {
        try {
            Long userId = getCurrentUserId();
            logger.info("Deleting logo for club ID: {} by user ID: {}", clubId, userId);
            
            Club club = clubService.deleteClubLogo(clubId, userId);
            ClubDto clubDto = clubMapper.toClubDto(club);
            
            return buildResponse.success(clubDto, "Club logo deleted successfully");
            
        } catch (Exception e) {
            logger.error("Failed to delete logo for club ID: {}", clubId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("delete", e.getMessage());
            return buildResponse.error("Failed to delete logo", errors);
        }
    }

    @Operation(summary = "Delete club main photo", 
               description = "Delete club main photo (owner/manager only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Photo deleted successfully"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "403", description = "User not authorized (must be club owner/manager)")
    })
    @DeleteMapping("/{clubId}/photo")
    @Override
    public ResponseEntity<Object> deleteClubMainPhoto(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId) {
        try {
            Long userId = getCurrentUserId();
            logger.info("Deleting main photo for club ID: {} by user ID: {}", clubId, userId);
            
            Club club = clubService.deleteClubMainPhoto(clubId, userId);
            ClubDto clubDto = clubMapper.toClubDto(club);
            
            return buildResponse.success(clubDto, "Club main photo deleted successfully");
            
        } catch (Exception e) {
            logger.error("Failed to delete main photo for club ID: {}", clubId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("delete", e.getMessage());
            return buildResponse.error("Failed to delete photo", errors);
        }
    }

    @Operation(summary = "Search clubs by name", description = "Search clubs by name with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clubs retrieved successfully")
    })
    @GetMapping("/search")
    @Override
    public ResponseEntity<Object> searchClubsByName(
            @Parameter(description = "Search query") @RequestParam String query,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        try {
            logger.info("Searching clubs with query: '{}', page: {}, size: {}", query, page, size);
            
            PageRequest pageRequest = PageRequest.of(page, size);
            List<Club> clubs = clubService.searchClubsByName(query, pageRequest);
            List<ClubDto> clubDtos = clubs.stream()
                    .map(clubMapper::toBasicClubDto)
                    .toList();
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("clubs", clubDtos);
            responseData.put("totalElements", clubDtos.size());
            responseData.put("currentPage", page);
            responseData.put("pageSize", size);
            
            return buildResponse.success(responseData, "Clubs retrieved successfully");
            
        } catch (Exception e) {
            logger.error("Failed to search clubs with query: '{}'", query, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("search", e.getMessage());
            return buildResponse.error("Failed to search clubs", errors);
        }
    }

    @Operation(summary = "Get clubs by county", description = "Get clubs in a specific county with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clubs retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "County not found")
    })
    @GetMapping("/county/{countyId}")
    @Override
    public ResponseEntity<Object> getClubsByCounty(
            @Parameter(description = "County ID", required = true) @PathVariable Long countyId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        try {
            logger.info("Fetching clubs for county ID: {}, page: {}, size: {}", countyId, page, size);
            
            PageRequest pageRequest = PageRequest.of(page, size);
            List<Club> clubs = clubService.getClubsByCounty(countyId, pageRequest);
            List<ClubDto> clubDtos = clubs.stream()
                    .map(clubMapper::toBasicClubDto)
                    .toList();
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("clubs", clubDtos);
            responseData.put("totalElements", clubDtos.size());
            responseData.put("currentPage", page);
            responseData.put("pageSize", size);
            responseData.put("countyId", countyId);
            
            return buildResponse.success(responseData, "Clubs retrieved successfully");
            
        } catch (Exception e) {
            logger.error("Failed to fetch clubs for county ID: {}", countyId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("county", e.getMessage());
            return buildResponse.error("Failed to fetch clubs by county", errors);
        }
    }

    @Operation(summary = "Get clubs by region", description = "Get clubs in a specific region with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clubs retrieved successfully")
    })
    @GetMapping("/region/{region}")
    @Override
    public ResponseEntity<Object> getClubsByRegion(
            @Parameter(description = "Region name", required = true) @PathVariable String region,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        try {
            logger.info("Fetching clubs for region: {}, page: {}, size: {}", region, page, size);
            
            PageRequest pageRequest = PageRequest.of(page, size);
            List<Club> clubs = clubService.getClubsByRegion(region, pageRequest);
            List<ClubDto> clubDtos = clubs.stream()
                    .map(clubMapper::toBasicClubDto)
                    .toList();
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("clubs", clubDtos);
            responseData.put("totalElements", clubDtos.size());
            responseData.put("currentPage", page);
            responseData.put("pageSize", size);
            responseData.put("region", region);
            
            return buildResponse.success(responseData, "Clubs retrieved successfully");
            
        } catch (Exception e) {
            logger.error("Failed to fetch clubs for region: {}", region, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("region", e.getMessage());
            return buildResponse.error("Failed to fetch clubs by region", errors);
        }
    }

    @Operation(summary = "Get clubs by level", description = "Get clubs by level with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clubs retrieved successfully")
    })
    @GetMapping("/level/{level}")
    @Override
    public ResponseEntity<Object> getClubsByLevel(
            @Parameter(description = "Club level", required = true) @PathVariable Club.ClubLevel level,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        try {
            logger.info("Fetching clubs for level: {}, page: {}, size: {}", level, page, size);
            
            PageRequest pageRequest = PageRequest.of(page, size);
            List<Club> clubs = clubService.getClubsByLevel(level, pageRequest);
            List<ClubDto> clubDtos = clubs.stream()
                    .map(clubMapper::toBasicClubDto)
                    .toList();
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("clubs", clubDtos);
            responseData.put("totalElements", clubDtos.size());
            responseData.put("currentPage", page);
            responseData.put("pageSize", size);
            responseData.put("level", level);
            
            return buildResponse.success(responseData, "Clubs retrieved successfully");
            
        } catch (Exception e) {
            logger.error("Failed to fetch clubs for level: {}", level, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("level", e.getMessage());
            return buildResponse.error("Failed to fetch clubs by level", errors);
        }
    }

    @Operation(summary = "Get clubs near location", 
               description = "Get clubs near geographic location with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clubs retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid coordinates")
    })
    @GetMapping("/near")
    @Override
    public ResponseEntity<Object> getClubsNearLocation(
            @Parameter(description = "Latitude", required = true) @RequestParam BigDecimal latitude,
            @Parameter(description = "Longitude", required = true) @RequestParam BigDecimal longitude,
            @Parameter(description = "Search radius in kilometers") @RequestParam(defaultValue = "50.0") double radiusKm,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        try {
            logger.info("Fetching clubs near location: {}, {}, radius: {}km, page: {}, size: {}", 
                       latitude, longitude, radiusKm, page, size);
            
            PageRequest pageRequest = PageRequest.of(page, size);
            List<Club> clubs = clubService.getClubsNearLocation(latitude, longitude, radiusKm, pageRequest);
            List<ClubDto> clubDtos = clubs.stream()
                    .map(clubMapper::toBasicClubDto)
                    .toList();
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("clubs", clubDtos);
            responseData.put("totalElements", clubDtos.size());
            responseData.put("currentPage", page);
            responseData.put("pageSize", size);
            responseData.put("searchLocation", Map.of("latitude", latitude, "longitude", longitude));
            responseData.put("radiusKm", radiusKm);
            
            return buildResponse.success(responseData, "Clubs retrieved successfully");
            
        } catch (Exception e) {
            logger.error("Failed to fetch clubs near location: {}, {}", latitude, longitude, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("location", e.getMessage());
            return buildResponse.error("Failed to fetch clubs by location", errors);
        }
    }

    @Operation(summary = "Get verified clubs", 
               description = "Get verified clubs with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verified clubs retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid verification type")
    })
    @GetMapping("/verified")
    @Override
    public ResponseEntity<Object> getVerifiedClubs(
            @Parameter(description = "Verification type: 'ligiopen', 'fkf', or 'both'") 
            @RequestParam(defaultValue = "ligiopen") String verificationType,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        try {
            logger.info("Fetching verified clubs, type: {}, page: {}, size: {}", verificationType, page, size);
            
            PageRequest pageRequest = PageRequest.of(page, size);
            List<Club> clubs = clubService.getVerifiedClubs(verificationType, pageRequest);
            List<ClubDto> clubDtos = clubs.stream()
                    .map(clubMapper::toBasicClubDto)
                    .toList();
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("clubs", clubDtos);
            responseData.put("totalElements", clubDtos.size());
            responseData.put("currentPage", page);
            responseData.put("pageSize", size);
            responseData.put("verificationType", verificationType);
            
            return buildResponse.success(responseData, "Verified clubs retrieved successfully");
            
        } catch (Exception e) {
            logger.error("Failed to fetch verified clubs with type: {}", verificationType, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("verification", e.getMessage());
            return buildResponse.error("Failed to fetch verified clubs", errors);
        }
    }

    @Operation(summary = "Get user's owned clubs", 
               description = "Get clubs owned by the current user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Owned clubs retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @GetMapping("/my-clubs/owned")
    @Override
    public ResponseEntity<Object> getUserOwnedClubs() {
        try {
            Long userId = getCurrentUserId();
            logger.info("Fetching owned clubs for user ID: {}", userId);
            
            List<Club> clubs = clubService.getClubsOwnedByUser(userId);
            List<ClubDto> clubDtos = clubs.stream()
                    .map(clubMapper::toClubDto)
                    .toList();
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("clubs", clubDtos);
            responseData.put("totalElements", clubDtos.size());
            
            return buildResponse.success(responseData, "Owned clubs retrieved successfully");
            
        } catch (Exception e) {
            logger.error("Failed to fetch owned clubs for user", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("ownedClubs", e.getMessage());
            return buildResponse.error("Failed to fetch owned clubs", errors);
        }
    }

    @Operation(summary = "Get user's managed clubs", 
               description = "Get clubs managed by the current user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Managed clubs retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @GetMapping("/my-clubs/managed")
    @Override
    public ResponseEntity<Object> getUserManagedClubs() {
        try {
            Long userId = getCurrentUserId();
            logger.info("Fetching managed clubs for user ID: {}", userId);
            
            List<Club> clubs = clubService.getClubsManagedByUser(userId);
            List<ClubDto> clubDtos = clubs.stream()
                    .map(clubMapper::toClubDto)
                    .toList();
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("clubs", clubDtos);
            responseData.put("totalElements", clubDtos.size());
            
            return buildResponse.success(responseData, "Managed clubs retrieved successfully");
            
        } catch (Exception e) {
            logger.error("Failed to fetch managed clubs for user", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("managedClubs", e.getMessage());
            return buildResponse.error("Failed to fetch managed clubs", errors);
        }
    }

    @Operation(summary = "Get club statistics", 
               description = "Get detailed statistics for a club")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Club statistics retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Club not found")
    })
    @GetMapping("/{clubId}/statistics")
    @Override
    public ResponseEntity<Object> getClubStatistics(
            @Parameter(description = "Club ID", required = true) @PathVariable Long clubId) {
        try {
            logger.info("Fetching statistics for club ID: {}", clubId);
            
            Map<String, Object> statistics = clubService.getClubStatistics(clubId);
            
            return buildResponse.success(statistics, "Club statistics retrieved successfully");
            
        } catch (Exception e) {
            logger.error("Failed to fetch statistics for club ID: {}", clubId, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("statistics", e.getMessage());
            return buildResponse.error("Failed to fetch club statistics", errors);
        }
    }

    @Operation(summary = "Get all active clubs", 
               description = "Get all active clubs with pagination (public endpoint)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Active clubs retrieved successfully")
    })
    @GetMapping("/active")
    @Override
    public ResponseEntity<Object> getAllActiveClubs(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        try {
            logger.info("Fetching all active clubs, page: {}, size: {}", page, size);
            
            PageRequest pageRequest = PageRequest.of(page, size);
            List<Club> clubs = clubService.getAllActiveClubs(pageRequest);
            List<ClubDto> clubDtos = clubs.stream()
                    .map(clubMapper::toBasicClubDto)
                    .toList();
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("clubs", clubDtos);
            responseData.put("totalElements", clubDtos.size());
            responseData.put("currentPage", page);
            responseData.put("pageSize", size);
            
            return buildResponse.success(responseData, "Active clubs retrieved successfully");
            
        } catch (Exception e) {
            logger.error("Failed to fetch active clubs", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("activeClubs", e.getMessage());
            return buildResponse.error("Failed to fetch active clubs", errors);
        }
    }

    @Operation(summary = "Get club by registration number", 
               description = "Get club by FKF registration number (for verification)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Club retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Club not found")
    })
    @GetMapping("/registration/{registrationNumber}")
    @Override
    public ResponseEntity<Object> getClubByRegistrationNumber(
            @Parameter(description = "FKF Registration number", required = true) 
            @PathVariable String registrationNumber) {
        try {
            logger.info("Fetching club with registration number: {}", registrationNumber);
            
            Club club = clubService.getClubByRegistrationNumberOrThrow(registrationNumber);
            ClubDto clubDto = clubMapper.toClubDto(club);
            
            return buildResponse.success(clubDto, "Club retrieved successfully");
            
        } catch (Exception e) {
            logger.error("Failed to fetch club with registration number: {}", registrationNumber, e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("registrationNumber", e.getMessage());
            return buildResponse.error("Failed to fetch club", errors);
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