package com.jabulani.ligiopen.dto.player;

import com.jabulani.ligiopen.entity.player.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDto {

    private Long id;
    
    private String firstName;
    
    private String lastName;
    
    private String fullName;
    
    private String nickname;
    
    private LocalDate dateOfBirth;
    
    private Integer age;
    
    private String placeOfBirth;
    
    private String nationality;
    
    private String secondNationality;
    
    private Double heightCm;
    
    private Double weightKg;
    
    private Player.PreferredFoot preferredFoot;
    
    private Player.Position primaryPosition;
    
    private List<Player.Position> secondaryPositions;
    
    private String mainPhotoUrl;
    
    private List<String> photoUrls;
    
    private String bio;
    
    private String phoneNumber;
    
    private String email;
    
    private String emergencyContactName;
    
    private String emergencyContactPhone;
    
    private String emergencyContactRelationship;
    
    private String idNumber;
    
    private String passportNumber;
    
    private String fkfRegistrationNumber;
    
    private BigDecimal marketValue;
    
    private String marketValueFormatted;
    
    private Boolean isActive;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // Current club information (if applicable)
    private Long currentClubId;
    
    private String currentClubName;
    
    private String currentClubLogo;
    
    private Integer currentJerseyNumber;
    
    private Player.Position currentPosition;
    
    private Boolean isCaptain;
    
    private Boolean isViceCaptain;
    
    private LocalDate joinedCurrentClubDate;
    
    private String membershipStatus;
    
    private String contractType;
    
    private LocalDate contractEndDate;
    
    // Career statistics
    private Integer totalClubs;
    
    private Integer totalTransfers;
    
    private LocalDate careerStartDate;
    
    private String status; // "Free Agent", "Active", "Injured", "Suspended", etc.
    
    private Boolean availableForTransfer;
    
    // Additional computed fields
    private String displayName; // Preferred name for UI display
    
    private String heightFormatted; // e.g., "1.85m"
    
    private String weightFormatted; // e.g., "75kg"
    
    private String ageGroup; // e.g., "Youth", "Senior", "Veteran"
    
    private String experienceLevel; // e.g., "Beginner", "Experienced", "Professional"
}