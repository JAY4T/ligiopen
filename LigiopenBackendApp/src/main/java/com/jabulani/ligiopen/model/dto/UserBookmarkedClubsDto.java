package com.jabulani.ligiopen.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBookmarkedClubsDto {
    private Integer userId;
    private String username;
    private List<ClubDetailsMin> clubs;
}
