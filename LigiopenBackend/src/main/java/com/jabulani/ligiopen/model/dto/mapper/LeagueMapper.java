package com.jabulani.ligiopen.model.dto.mapper;

import com.jabulani.ligiopen.model.club.entity.League;
import com.jabulani.ligiopen.model.dto.LeagueDetailsDto;
import com.jabulani.ligiopen.model.dto.LeagueDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class LeagueMapper {
    private final ClubMapper clubMapper;

    @Autowired
    public LeagueMapper(@Lazy ClubMapper clubMapper) {
        this.clubMapper = clubMapper;
    }

    public LeagueDto leagueDto(League league) {
        return LeagueDto.builder()
                .id(league.getId())
                .name(league.getName())
                .build();
    }

    public LeagueDetailsDto leagueDetailsDto(League league) {
        return LeagueDetailsDto.builder()
                .id(league.getId())
                .name(league.getName())
//                .clubs(league.getClubs().stream().map(clubMapper::clubDetailsDto).collect(Collectors.toList()))
                .build();
    }
}
