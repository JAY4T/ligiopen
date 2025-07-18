package com.jabulani.ligiopen.controller.league;

import com.jabulani.ligiopen.config.response.BuildResponse;
import com.jabulani.ligiopen.config.response.Response;
import com.jabulani.ligiopen.model.dto.AddLeagueDto;
import com.jabulani.ligiopen.service.league.LeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class LeagueControllerImpl implements LeagueController{
    private final BuildResponse buildResponse;
    private final LeagueService leagueService;

    @Autowired
    public LeagueControllerImpl(
            BuildResponse buildResponse,
            LeagueService leagueService
    ){
        this.buildResponse = buildResponse;
        this.leagueService = leagueService;
    }

    @PostMapping("league")
    @Override
    public ResponseEntity<Response> createLeague(@RequestBody AddLeagueDto addLeagueDto) {
        return buildResponse.createResponse("league", leagueService.createLeague(addLeagueDto), "League created", HttpStatus.OK);
    }

    @GetMapping("league/id/{leagueId}")
    @Override
    public ResponseEntity<Response> getLeagueById(@PathVariable("leagueId") Integer id) {
        return buildResponse.createResponse("league", leagueService.getLeagueById(id), "League retrieved", HttpStatus.OK);
    }

    @GetMapping("league/name/{name}")
    @Override
    public ResponseEntity<Response> getLeagueByName(@PathVariable("name") String name) {
        return buildResponse.createResponse("name", leagueService.getLeagueByName(name), "League retrieved", HttpStatus.OK);
    }

    @GetMapping("league/exist/{name}")
    @Override
    public ResponseEntity<Response> leagueExists(@PathVariable("name") String name) {
        return buildResponse.createResponse("league", leagueService.leagueExists(name), "League retrieved", HttpStatus.OK);
    }

    @GetMapping("league/all")
    @Override
    public ResponseEntity<Response> getLeagues() {
        return buildResponse.createResponse("league", leagueService.getLeagues(), "League retrieved", HttpStatus.OK);
    }
}
