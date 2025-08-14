package com.jabulani.ligiopen.dto.player;

import com.jabulani.ligiopen.entity.player.ClubMembership;
import com.jabulani.ligiopen.entity.player.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClubMembershipDto {

    private Long id;
    
    private Long playerId;
    
    private String playerName;
    
    private String playerPhotoUrl;
    
    private Long clubId;
    
    private String clubName;
    
    private String clubLogo;
    
    private Integer jerseyNumber;
    
    private Player.Position position;
    
    private Boolean isActive;
    
    private LocalDate joinedDate;
    
    private LocalDate leftDate;
    
    private Boolean isCaptain;
    
    private Boolean isViceCaptain;
    
    private ClubMembership.ContractType contractType;
    
    private LocalDate contractStartDate;
    
    private LocalDate contractEndDate;
    
    private BigDecimal weeklyWage;
    
    private String weeklyWageFormatted;
    
    private BigDecimal signingFee;
    
    private String signingFeeFormatted;
    
    private ClubMembership.MembershipStatus membershipStatus;
    
    private String statusDescription;
    
    private Long durationInDays;
    
    private String durationFormatted; // e.g., "2 years, 3 months"
    
    private Boolean isCurrentMembership;
    
    private Boolean contractExpiringSoon; // Within 6 months
    
    private String roleDescription; // e.g., "Captain", "Vice Captain", "Player"
}