package com.jabulani.ligiopen.dto.club;

import com.jabulani.ligiopen.entity.club.Club;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClubVerificationDto {

    @NotNull(message = "Verification status is required")
    private Boolean verified;

    @Size(max = 500, message = "Verification notes cannot exceed 500 characters")
    private String notes;
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class FkfVerificationDto {

    @NotNull(message = "FKF verification status is required")
    private Club.FkfVerificationStatus fkfVerificationStatus;
}