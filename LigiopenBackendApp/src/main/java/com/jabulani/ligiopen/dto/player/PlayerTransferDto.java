package com.jabulani.ligiopen.dto.player;

import com.jabulani.ligiopen.entity.player.PlayerTransfer;
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
public class PlayerTransferDto {

    private Long id;
    
    private Long playerId;
    
    private String playerName;
    
    private String playerPhotoUrl;
    
    private Long fromClubId;
    
    private String fromClubName;
    
    private String fromClubLogo;
    
    private Long toClubId;
    
    private String toClubName;
    
    private String toClubLogo;
    
    private LocalDate transferDate;
    
    private BigDecimal transferFee;
    
    private String transferFeeFormatted;
    
    private BigDecimal agentFee;
    
    private String agentFeeFormatted;
    
    private LocalDate contractUntil;
    
    private PlayerTransfer.TransferType transferType;
    
    private String transferTypeDescription;
    
    private PlayerTransfer.TransferStatus transferStatus;
    
    private String transferStatusDescription;
    
    private String transferWindow;
    
    private LocalDate announcementDate;
    
    private Boolean medicalCompleted;
    
    private Boolean contractSigned;
    
    private String notes;
    
    private String transferDescription; // e.g., "Free transfer from Club A to Club B"
    
    private Boolean isPermanent;
    
    private Boolean isLoan;
    
    private Boolean isFreeTransfer;
    
    private String statusBadgeColor; // For UI styling
    
    private Long daysInProcess; // Days between announcement and completion
    
    private Boolean isRecentTransfer; // Within last 30 days
}