package com.jabulani.ligiopen.dto.location;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StadiumSubmissionDto {

    @NotBlank(message = "Stadium name is required")
    @Size(min = 2, max = 100, message = "Stadium name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City name cannot exceed 50 characters")
    private String city;

    @Size(max = 50, message = "Town name cannot exceed 50 characters")
    private String town;

    @NotNull(message = "County ID is required")
    private Long countyId;

    @NotNull(message = "Capacity is required")
    @Min(value = 50, message = "Capacity must be at least 50")
    private Integer capacity;

    private String surfaceType = "NATURAL_GRASS";

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private Boolean hasFloodlights = false;
    private Boolean hasParking = false;
    private Boolean hasConcessions = false;
    private Boolean hasWifiInternet = false;

    @Size(max = 200, message = "Address cannot exceed 200 characters")
    private String address;

    // GPS coordinates (highly recommended for duplicate detection)
    private Double latitude;
    private Double longitude;

    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    private String phoneNumber;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Size(max = 200, message = "Website URL cannot exceed 200 characters")
    private String websiteUrl;

    @Size(max = 100, message = "Owner name cannot exceed 100 characters")
    private String ownerName;

    @Size(max = 100, message = "Management company cannot exceed 100 characters")
    private String managementCompany;

    @Size(max = 500, message = "Additional notes cannot exceed 500 characters")
    private String notes; // Why submitting this stadium, additional info
}