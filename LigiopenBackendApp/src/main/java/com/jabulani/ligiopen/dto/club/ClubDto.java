package com.jabulani.ligiopen.dto.club;

import com.jabulani.ligiopen.entity.club.Club;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClubDto {

    private Long id;
    
    private String name;
    
    private String shortName;
    
    private String abbreviation;
    
    private LocalDate founded;
    
    private String description;
    
    private String colors;
    
    private String registrationNumber;
    
    private String contactEmail;
    
    private String contactPhone;
    
    private String websiteUrl;
    
    private String socialMediaLinks;
    
    private Club.ClubLevel clubLevel;
    
    // Location information
    private Long countyId;
    private String countyName;
    private String region;
    
    private Long homeStadiumId;
    private String homeStadiumName;
    
    // Owner information
    private Long ownerId;
    private String ownerName;
    private String ownerEmail;
    
    // Verification status
    private Boolean isLigiopenVerified;
    private Club.LigiopenVerificationStatus ligiopenVerificationStatus;
    private LocalDateTime ligiopenVerificationDate;
    private String ligiopenVerificationNotes;
    
    private Boolean isFkfVerified;
    private Club.FkfVerificationStatus fkfVerificationStatus;
    private LocalDateTime fkfVerificationDate;
    private LocalDate fkfRegistrationDate;
    
    // Media
    private Long logoFileId;
    private String logoUrl;
    private Long mainPhotoId;
    private String mainPhotoUrl;
    
    // Status
    private Boolean isActive;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Statistics (optional - for detailed view)
    private Integer managersCount;
    private Integer membersCount;
    private Integer favoritesCount;
}