package com.jabulani.ligiopen.service.league;

import com.jabulani.ligiopen.model.club.entity.League;
import com.jabulani.ligiopen.model.dto.AddLeagueDto;
import com.jabulani.ligiopen.model.dto.LeagueDetailsDto;
import com.jabulani.ligiopen.model.dto.LeagueDto;

import java.util.List;

public interface LeagueService {
    LeagueDetailsDto createLeague(AddLeagueDto addLeagueDto);
//    League updateLeague(League league);
    LeagueDetailsDto getLeagueById(Integer id);
    LeagueDetailsDto getLeagueByName(String name);
    Boolean leagueExists(String name);
    List<LeagueDetailsDto> getLeagues();
}
