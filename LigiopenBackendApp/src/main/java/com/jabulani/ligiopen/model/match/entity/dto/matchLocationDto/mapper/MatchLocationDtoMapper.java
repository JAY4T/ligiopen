package com.jabulani.ligiopen.model.match.entity.dto.matchLocationDto.mapper;

import com.jabulani.ligiopen.model.aws.dto.FileDto;
import com.jabulani.ligiopen.model.aws.dto.mapper.FileMapper;
import com.jabulani.ligiopen.model.dto.mapper.ClubMapper;
import com.jabulani.ligiopen.model.match.entity.MatchFixture;
import com.jabulani.ligiopen.model.match.entity.MatchLocation;
import com.jabulani.ligiopen.model.match.entity.dto.matchLocationDto.MatchLocationDto;
import com.jabulani.ligiopen.model.match.entity.dto.matchLocationDto.MatchLocationDtoMax;
import com.jabulani.ligiopen.service.aws.AwsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MatchLocationDtoMapper {
    private final String BUCKET_NAME = "ligiopen";

    private final AwsService awsService;

    private final FileMapper fileMapper;


    private final ClubMapper clubMapper;

    @Autowired
    public MatchLocationDtoMapper(
            AwsService awsService,
            FileMapper fileMapper,
            ClubMapper clubMapper

    ) {
        this.awsService = awsService;
        this.fileMapper = fileMapper;
        this.clubMapper = clubMapper;
    }

    public MatchLocationDto matchLocationDto(MatchLocation matchLocation) {
        List<FileDto> files = new ArrayList<>();
        if(!matchLocation.getLocationPhotos().isEmpty()) {
            files.addAll(matchLocation.getLocationPhotos().stream().map(fileMapper::fileDto).toList());
        }
        return MatchLocationDto.builder()
                .locationId(matchLocation.getId())
                .venueName(matchLocation.getVenueName())
                .country(matchLocation.getCountry())
                .county(matchLocation.getCounty())
                .town(matchLocation.getTown())
                .photos(files)
                .build();
    }

    public MatchLocationDtoMax matchLocationDtoMax(MatchLocation matchLocation) {
        List<FileDto> files = new ArrayList<>();
        if(!matchLocation.getLocationPhotos().isEmpty()) {
            files.addAll(matchLocation.getLocationPhotos().stream().map(fileMapper::fileDto).toList());
        }
        return MatchLocationDtoMax.builder()
                .locationId(matchLocation.getId())
                .venueName(matchLocation.getVenueName())
                .country(matchLocation.getCountry())
                .county(matchLocation.getCounty())
                .town(matchLocation.getTown())
                .photos(files)
                .matchFixturesIds(matchLocation.getMatchFixtures().stream().map(MatchFixture::getId).collect(Collectors.toList()))
                .clubs(matchLocation.getClubs().stream().map(clubMapper::clubDetailsDto).collect(Collectors.toList()))
                .build();
    }
}
