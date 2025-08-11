package com.jabulani.ligiopen.dto.club;

import com.jabulani.ligiopen.entity.club.Club;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClubRegistrationDto {

    @NotBlank(message = "Club name is required")
    @Size(min = 2, max = 100, message = "Club name must be between 2 and 100 characters")
    private String name;

    @Size(max = 20, message = "Short name cannot exceed 20 characters")
    private String shortName;

    @Size(max = 10, message = "Abbreviation cannot exceed 10 characters")
    private String abbreviation;

    @NotNull(message = "County ID is required")
    private Long countyId;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotBlank(message = "Contact email is required")
    @Email(message = "Invalid email format")
    private String contactEmail;

    @Size(max = 20, message = "Contact phone cannot exceed 20 characters")
    private String contactPhone;

    @Size(max = 50, message = "Colors description cannot exceed 50 characters")
    private String colors;

    // For FKF registered clubs
    private String registrationNumber;

    private Club.ClubLevel clubLevel;

    private LocalDate founded;

    @Size(max = 200, message = "Website URL cannot exceed 200 characters")
    private String websiteUrl;

    @Size(max = 1000, message = "Social media links cannot exceed 1000 characters")
    private String socialMediaLinks;

    private Long homeStadiumId;
}