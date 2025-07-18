package com.jabulani.ligiopen.model.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SetTeamAdminDto {
    private Integer userId;
    private Integer teamId;
}
