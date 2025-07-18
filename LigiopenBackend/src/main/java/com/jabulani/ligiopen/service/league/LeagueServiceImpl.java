package com.jabulani.ligiopen.service.league;

import com.google.gson.Gson;
import com.jabulani.ligiopen.dao.league.LeagueDao;
import com.jabulani.ligiopen.model.club.entity.League;
import com.jabulani.ligiopen.model.dto.AddLeagueDto;
import com.jabulani.ligiopen.model.dto.LeagueDetailsDto;
import com.jabulani.ligiopen.model.dto.mapper.LeagueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeagueServiceImpl implements LeagueService{
    private final LeagueDao leagueDao;
    private final LeagueMapper leagueMapper;

    @Autowired
    public LeagueServiceImpl(
            LeagueDao leagueDao,
            LeagueMapper leagueMapper
    ) {
        this.leagueDao = leagueDao;
        this.leagueMapper = leagueMapper;
    }

    @Transactional
    @Override
    public LeagueDetailsDto createLeague(AddLeagueDto addLeagueDto) {
        League league = League.builder()
                .name(addLeagueDto.getName())
                .clubs(new ArrayList<>())
                .build();

        return leagueMapper.leagueDetailsDto(leagueDao.createLeague(league));
    }

    @Override
    public LeagueDetailsDto getLeagueById(Integer id) {
        return leagueMapper.leagueDetailsDto(leagueDao.getLeagueById(id));
    }

    @Override
    public LeagueDetailsDto getLeagueByName(String name) {
        return leagueMapper.leagueDetailsDto(leagueDao.getLeagueByName(name));
    }

    @Override
    public Boolean leagueExists(String name) {
        return leagueDao.leagueExists(name);
    }

    @Override
    public List<LeagueDetailsDto> getLeagues() {
        return leagueDao.getLeagues().stream().map(leagueMapper::leagueDetailsDto).collect(Collectors.toList());
    }


}
