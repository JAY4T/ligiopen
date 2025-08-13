package com.jabulani.ligiopen.dto.club;

import com.jabulani.ligiopen.entity.club.Club;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^[+]?[0-9\\-\\s\\(\\)]*$", message = "Invalid phone number format")
    private String contactPhone;

    @Size(max = 50, message = "Colors description cannot exceed 50 characters")
    private String colors;

    private LocalDate founded;

    @Size(max = 200, message = "Website URL cannot exceed 200 characters")
    @Pattern(regexp = "^(https?://)?[\\w\\.-]+\\.[a-z]{2,}(/.*)?$", message = "Invalid website URL format")
    private String websiteUrl;

    @Size(max = 1000, message = "Social media links cannot exceed 1000 characters")
    private String socialMediaLinks;

    private Long homeStadiumId;

    // Club Level (determines if grassroots or higher)
    @NotNull(message = "Club level is required")
    private Club.ClubLevel clubLevel;

    // FKF Registration Information (optional - only for FKF registered clubs)
    private Boolean isFkfRegistered = false;

    @Size(max = 50, message = "FKF registration number cannot exceed 50 characters")
    private String fkfRegistrationNumber;

    private LocalDate fkfRegistrationDate;

    // Note: currentLeague and tier are stored in separate competition/season entities
    // These are kept for future use but not stored directly in the club entity
    @Size(max = 100, message = "League name cannot exceed 100 characters")
    private String currentLeague;

    private Integer tier;
}