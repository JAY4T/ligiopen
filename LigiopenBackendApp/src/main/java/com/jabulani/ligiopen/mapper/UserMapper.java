package com.jabulani.ligiopen.mapper;

import com.jabulani.ligiopen.dto.user.UserDto;
import com.jabulani.ligiopen.dto.user.UserProfileDto;
import com.jabulani.ligiopen.entity.user.UserEntity;
import com.jabulani.ligiopen.service.file.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    
    private final FileUploadService fileUploadService;
    
    @Autowired
    public UserMapper(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }
    
    public UserDto toUserDto(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        
        return UserDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .phoneNumber(userEntity.getPhoneNumber())
                .bio(userEntity.getBio())
                .preferredLanguage(userEntity.getPreferredLanguage())
                .profilePictureId(userEntity.getProfilePictureId())
                .emailVerified(userEntity.isEmailVerified())
                .accountEnabled(userEntity.isAccountEnabled())
                .role(userEntity.getRole() != null ? userEntity.getRole().name() : null)
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .build();
    }
    
    public UserEntity toUserEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        
        return UserEntity.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .phoneNumber(userDto.getPhoneNumber())
                .bio(userDto.getBio())
                .preferredLanguage(userDto.getPreferredLanguage())
                .profilePictureId(userDto.getProfilePictureId())
                .emailVerified(userDto.isEmailVerified())
                .accountEnabled(userDto.isAccountEnabled())
                .role(userDto.getRole() != null ? UserEntity.UserRole.valueOf(userDto.getRole()) : null)
                .createdAt(userDto.getCreatedAt())
                .updatedAt(userDto.getUpdatedAt())
                .build();
    }
    
    public UserProfileDto toUserProfileDto(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        
        return UserProfileDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .phoneNumber(userEntity.getPhoneNumber())
                .bio(userEntity.getBio())
                .preferredLanguage(userEntity.getPreferredLanguage())
                .profilePictureUrl(generateProfilePictureUrl(userEntity.getProfilePictureId()))
                .emailVerified(userEntity.isEmailVerified())
                .accountEnabled(userEntity.isAccountEnabled())
                .role(userEntity.getRole() != null ? userEntity.getRole().name() : null)
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .favoritedClubsCount(userEntity.getFavoritedClubs() != null ? userEntity.getFavoritedClubs().size() : 0)
                .ownedClubsCount(userEntity.getOwnedClubs() != null ? userEntity.getOwnedClubs().size() : 0)
                .managedClubsCount(userEntity.getManagedClubs() != null ? userEntity.getManagedClubs().size() : 0)
                .build();
    }
    
    private String generateProfilePictureUrl(Long profilePictureId) {
        if (profilePictureId == null) {
            return null;
        }
        try {
            return fileUploadService.getFileUrl(profilePictureId.intValue());
        } catch (Exception e) {
            // Return null if file not found or error getting URL
            return null;
        }
    }
}