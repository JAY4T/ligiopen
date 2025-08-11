package com.jabulani.ligiopen.dto.club;

import com.jabulani.ligiopen.entity.club.Club;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
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
public class ClubUpdateDto {

    @Size(min = 2, max = 100, message = "Club name must be between 2 and 100 characters")
    private String name;

    @Size(min = 1, max = 20, message = "Short name must be between 1 and 20 characters")
    private String shortName;

    @Size(max = 10, message = "Abbreviation cannot exceed 10 characters")
    private String abbreviation;

    private LocalDate founded;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Size(max = 100, message = "Colors cannot exceed 100 characters")
    private String colors;

    @Email(message = "Contact email must be valid")
    @Size(max = 100, message = "Contact email cannot exceed 100 characters")
    private String contactEmail;

    @Pattern(regexp = "^(\\+254|0)[17]\\d{8}$", message = "Contact phone must be a valid Kenyan phone number")
    private String contactPhone;

    @Size(max = 200, message = "Website URL cannot exceed 200 characters")
    private String websiteUrl;

    @Size(max = 500, message = "Social media links cannot exceed 500 characters")
    private String socialMediaLinks;

    private Long countyId;

    private Long homeStadiumId;

    private Club.ClubLevel clubLevel;
}