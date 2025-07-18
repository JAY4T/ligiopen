package com.jabulani.ligiopen.controller.league;

import com.jabulani.ligiopen.config.response.Response;
import com.jabulani.ligiopen.model.dto.AddLeagueDto;
import org.springframework.http.ResponseEntity;

public interface LeagueController {
    ResponseEntity<Response> createLeague(AddLeagueDto addLeagueDto);
    //    League updateLeague(League league);
    ResponseEntity<Response> getLeagueById(Integer id);
    ResponseEntity<Response> getLeagueByName(String name);
    ResponseEntity<Response> leagueExists(String name);
    ResponseEntity<Response> getLeagues();
}
