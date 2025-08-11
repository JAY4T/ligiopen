package com.jabulani.ligiopen.dto.club;

import com.jabulani.ligiopen.entity.club.Club;
import com.jabulani.ligiopen.service.file.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClubMapper {

    private static final Logger logger = LoggerFactory.getLogger(ClubMapper.class);

    private final FileUploadService fileUploadService;

    @Autowired
    public ClubMapper(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    public ClubDto toClubDto(Club club) {
        if (club == null) {
            return null;
        }

        return ClubDto.builder()
                .id(club.getId())
                .name(club.getName())
                .shortName(club.getShortName())
                .abbreviation(club.getAbbreviation())
                .founded(club.getFounded())
                .description(club.getDescription())
                .colors(club.getColors())
                .registrationNumber(club.getRegistrationNumber())
                .contactEmail(club.getContactEmail())
                .contactPhone(club.getContactPhone())
                .websiteUrl(club.getWebsiteUrl())
                .socialMediaLinks(club.getSocialMediaLinks())
                .clubLevel(club.getClubLevel())
                // Location information
                .countyId(club.getCounty() != null ? club.getCounty().getId() : null)
                .countyName(club.getCounty() != null ? club.getCounty().getName() : null)
                .region(club.getCounty() != null ? club.getCounty().getRegion() : null)
                .homeStadiumId(club.getHomeStadium() != null ? club.getHomeStadium().getId() : null)
                .homeStadiumName(club.getHomeStadium() != null ? club.getHomeStadium().getName() : null)
                // Owner information
                .ownerId(club.getOwner() != null ? club.getOwner().getId() : null)
                .ownerName(club.getOwner() != null ? 
                    (club.getOwner().getFirstName() + " " + club.getOwner().getLastName()).trim() : null)
                .ownerEmail(club.getOwner() != null ? club.getOwner().getEmail() : null)
                // Verification status
                .isLigiopenVerified(club.getIsLigiopenVerified())
                .ligiopenVerificationStatus(club.getLigiopenVerificationStatus())
                .ligiopenVerificationDate(club.getLigiopenVerificationDate())
                .ligiopenVerificationNotes(club.getLigiopenVerificationNotes())
                .isFkfVerified(club.getIsFkfVerified())
                .fkfVerificationStatus(club.getFkfVerificationStatus())
                .fkfVerificationDate(club.getFkfVerificationDate())
                .fkfRegistrationDate(club.getFkfRegistrationDate())
                // Media
                .logoFileId(club.getLogoFileId())
                .logoUrl(generateFileUrl(club.getLogoFileId()))
                .mainPhotoId(club.getMainPhotoId())
                .mainPhotoUrl(generateFileUrl(club.getMainPhotoId()))
                // Status
                .isActive(club.getIsActive())
                // Timestamps
                .createdAt(club.getCreatedAt())
                .updatedAt(club.getUpdatedAt())
                // Statistics (would require additional queries in real implementation)
                .managersCount(club.getManagers() != null ? club.getManagers().size() : 0)
                .membersCount(0) // Would require member count query
                .favoritesCount(club.getFavoritedByUsers() != null ? club.getFavoritedByUsers().size() : 0)
                .build();
    }

    public ClubDto toBasicClubDto(Club club) {
        if (club == null) {
            return null;
        }

        return ClubDto.builder()
                .id(club.getId())
                .name(club.getName())
                .shortName(club.getShortName())
                .abbreviation(club.getAbbreviation())
                .clubLevel(club.getClubLevel())
                .countyId(club.getCounty() != null ? club.getCounty().getId() : null)
                .countyName(club.getCounty() != null ? club.getCounty().getName() : null)
                .region(club.getCounty() != null ? club.getCounty().getRegion() : null)
                .isLigiopenVerified(club.getIsLigiopenVerified())
                .isFkfVerified(club.getIsFkfVerified())
                .logoFileId(club.getLogoFileId())
                .logoUrl(generateFileUrl(club.getLogoFileId()))
                .isActive(club.getIsActive())
                .createdAt(club.getCreatedAt())
                .build();
    }

    private String generateFileUrl(Long fileId) {
        if (fileId == null) {
            return null;
        }
        
        try {
            return fileUploadService.getFileUrl(fileId.intValue());
        } catch (Exception e) {
            logger.warn("Failed to generate file URL for file ID: {}", fileId, e);
            return null;
        }
    }
}