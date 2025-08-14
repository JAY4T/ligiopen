package com.jabulani.ligiopen.dto.player;

import com.jabulani.ligiopen.entity.player.Player;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerRegistrationDto {

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Size(max = 30, message = "Nickname cannot exceed 30 characters")
    private String nickname;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @Size(max = 100, message = "Place of birth cannot exceed 100 characters")
    private String placeOfBirth;

    @NotBlank(message = "Nationality is required")
    @Size(max = 50, message = "Nationality cannot exceed 50 characters")
    private String nationality;

    @Size(max = 50, message = "Second nationality cannot exceed 50 characters")
    private String secondNationality;

    private Double heightCm;

    private Double weightKg;

    private Player.PreferredFoot preferredFoot;

    @NotNull(message = "Primary position is required")
    private Player.Position primaryPosition;

    private List<Player.Position> secondaryPositions;

    @Size(max = 1000, message = "Bio cannot exceed 1000 characters")
    private String bio;

    @Pattern(regexp = "^(\\+254|0)[17]\\d{8}$", message = "Phone number must be a valid Kenyan phone number")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Size(max = 100, message = "Emergency contact name cannot exceed 100 characters")
    private String emergencyContactName;

    @Pattern(regexp = "^(\\+254|0)[17]\\d{8}$", message = "Emergency contact phone must be a valid Kenyan phone number")
    private String emergencyContactPhone;

    @Size(max = 50, message = "Emergency contact relationship cannot exceed 50 characters")
    private String emergencyContactRelationship;

    @Size(max = 20, message = "ID number cannot exceed 20 characters")
    private String idNumber;

    @Size(max = 20, message = "Passport number cannot exceed 20 characters")
    private String passportNumber;

    @Size(max = 20, message = "FKF registration number cannot exceed 20 characters")
    private String fkfRegistrationNumber;

    private BigDecimal marketValue;
}