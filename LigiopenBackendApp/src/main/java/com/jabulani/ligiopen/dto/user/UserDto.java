package com.jabulani.ligiopen.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String bio;
    private String preferredLanguage;
    private Long profilePictureId;
    private boolean emailVerified;
    private boolean accountEnabled;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}