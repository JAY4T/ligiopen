package com.jabulani.ligiopen.dao.league;

import com.jabulani.ligiopen.model.club.entity.League;

import java.util.List;

public interface LeagueDao {
    League createLeague(League league);
    League updateLeague(League league);
    League getLeagueById(Integer id);
    League getLeagueByName(String name);
    Boolean leagueExists(String name);
    List<League> getLeagues();
}
