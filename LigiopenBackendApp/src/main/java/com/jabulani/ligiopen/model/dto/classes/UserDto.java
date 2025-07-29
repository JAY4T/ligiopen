package com.jabulani.ligiopen.model.dto.classes;

import com.jabulani.ligiopen.model.tables.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String googleEmail;
    private Boolean emailVerified;
    private Long profilePictureId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserEntity.UserRole role;
}
