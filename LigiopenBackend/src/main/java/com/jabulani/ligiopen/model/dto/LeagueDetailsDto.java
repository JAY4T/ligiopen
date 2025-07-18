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
public class LeagueDetailsDto {
    private Integer id;
    private String name;
//    private List<ClubDetailsDto> clubs;
}
