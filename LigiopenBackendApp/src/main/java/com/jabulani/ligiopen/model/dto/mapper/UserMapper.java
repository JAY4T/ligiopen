package com.jabulani.ligiopen.model.dto.mapper;

import com.jabulani.ligiopen.model.dto.classes.SignupRequestDto;
import com.jabulani.ligiopen.model.dto.classes.UserDto;
import com.jabulani.ligiopen.model.tables.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toUserDto(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .googleEmail(userEntity.getGoogleEmail())
                .accountEnabled(userEntity.isAccountEnabled())
                .emailVerified(userEntity.isEmailVerified())
                .profilePictureId(userEntity.getProfilePictureId())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .role(userEntity.getRole())
                .build();
    }
}
