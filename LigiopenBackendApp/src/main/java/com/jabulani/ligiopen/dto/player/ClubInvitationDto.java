package com.jabulani.ligiopen.dto.player;

import com.jabulani.ligiopen.entity.player.ClubInvitation;
import com.jabulani.ligiopen.entity.player.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClubInvitationDto {

    private Long id;
    
    private Long clubId;
    
    private String clubName;
    
    private String clubLogo;
    
    private Long playerId;
    
    private String playerName;
    
    private String playerPhotoUrl;
    
    private Long invitedByUserId;
    
    private String invitedByUserName;
    
    private ClubInvitation.InvitationStatus status;
    
    private String statusDescription;
    
    private LocalDateTime invitedAt;
    
    private LocalDateTime respondedAt;
    
    private LocalDateTime expiresAt;
    
    private String message;
    
    private Integer proposedJerseyNumber;
    
    private Player.Position proposedPosition;
    
    private String proposedPositionName;
    
    private BigDecimal proposedWage;
    
    private String proposedWageFormatted;
    
    private Boolean isExpired;
    
    private Boolean isPending;
    
    private Boolean isAccepted;
    
    private Boolean isRejected;
    
    private Boolean isCancelled;
    
    private Long hoursUntilExpiry;
    
    private String timeUntilExpiry; // e.g., "2 days, 5 hours"
    
    private String statusBadgeColor; // For UI styling
    
    private String invitationSummary; // e.g., "Invited by Club A as Striker"
    
    private Boolean canRespond; // Can player accept/reject
    
    private Boolean canCancel; // Can club cancel
}