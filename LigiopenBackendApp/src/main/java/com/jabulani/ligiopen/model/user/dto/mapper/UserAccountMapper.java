package com.jabulani.ligiopen.model.user.dto.mapper;

import com.jabulani.ligiopen.model.user.dto.UserAccountDto;
import com.jabulani.ligiopen.model.user.entity.UserAccount;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserAccountMapper {
    public UserAccountDto toUserDto(UserAccount userAccount) {
        Integer administeringClubId = null;

        if(userAccount.getManagedClub() != null) {
            administeringClubId = userAccount.getManagedClub().getId();
        }

        return UserAccountDto.builder()
                .id(userAccount.getId())
                .username(userAccount.getUsername())
                .email(userAccount.getEmail())
                .role(userAccount.getRole())
                .createdAt(userAccount.getCreatedAt())
                .archived(userAccount.getArchived())
                .archivedAt(userAccount.getArchivedAt())
                .administeringClubId(administeringClubId)
                .build();
    }
}
