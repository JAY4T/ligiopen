package com.jabulani.ligiopen.dto.player;

import com.jabulani.ligiopen.entity.player.PlayerTransfer;
import com.jabulani.ligiopen.entity.player.ClubMembership;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for creating transfer requests")
public class TransferRequestDto {
    
    @NotNull(message = "Player ID is required")
    @Positive(message = "Player ID must be positive")
    @Schema(description = "Player ID", example = "1")
    private Long playerId;
    
    @Schema(description = "Current club ID (null for free agents)", example = "1")
    private Long fromClubId;
    
    @NotNull(message = "Destination club ID is required")
    @Positive(message = "Destination club ID must be positive")
    @Schema(description = "Destination club ID", example = "2")
    private Long toClubId;
    
    @NotNull(message = "Transfer type is required")
    @Schema(description = "Transfer type", example = "PERMANENT")
    private PlayerTransfer.TransferType transferType;
    
    @NotNull(message = "Contract type is required")
    @Schema(description = "Contract type", example = "PROFESSIONAL")
    private ClubMembership.ContractType contractType;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Transfer fee must be positive")
    @Schema(description = "Transfer fee in KES", example = "500000.00")
    private BigDecimal transferFee;
    
    @NotNull(message = "Proposed salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Proposed salary must be positive")
    @Schema(description = "Proposed monthly salary in KES", example = "50000.00")
    private BigDecimal proposedSalary;
    
    @NotNull(message = "Contract duration is required")
    @Min(value = 1, message = "Contract duration must be at least 1 month")
    @Max(value = 60, message = "Contract duration cannot exceed 60 months")
    @Schema(description = "Contract duration in months", example = "24")
    private Integer contractDuration;
    
    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    @Schema(description = "Additional notes about the transfer", example = "Urgent transfer needed for upcoming season")
    private String notes;
}