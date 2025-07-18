package com.jabulani.ligiopen.service.match;

import com.jabulani.ligiopen.dao.club.ClubDao;
import com.jabulani.ligiopen.dao.club.PlayerDao;
import com.jabulani.ligiopen.dao.fileDao.FileDao;
import com.jabulani.ligiopen.dao.match.MatchDao;
import com.jabulani.ligiopen.model.aws.File;
import com.jabulani.ligiopen.model.club.entity.Club;
import com.jabulani.ligiopen.model.club.entity.Player;
import com.jabulani.ligiopen.model.club.entity.PlayerClub;
import com.jabulani.ligiopen.model.match.MatchEventType;
import com.jabulani.ligiopen.model.match.MatchStatus;
import com.jabulani.ligiopen.model.match.PlayerState;
import com.jabulani.ligiopen.model.match.entity.*;
import com.jabulani.ligiopen.model.match.entity.dto.PostMatchAnalysisStatusUpdateDto;
import com.jabulani.ligiopen.model.match.entity.events.*;
import com.jabulani.ligiopen.model.match.entity.dto.fixtureDto.MatchFixtureCreationDto;
import com.jabulani.ligiopen.model.match.entity.dto.fixtureDto.MatchFixtureDto;
import com.jabulani.ligiopen.model.match.entity.dto.fixtureDto.MatchFixtureStatusUpdateDto;
import com.jabulani.ligiopen.model.match.entity.dto.fixtureDto.MatchFixtureUpdateDto;
import com.jabulani.ligiopen.model.match.entity.dto.fixtureDto.mapper.MatchFixtureDtoMapper;
import com.jabulani.ligiopen.model.match.entity.dto.matchLocationDto.MatchLocationCreationDto;
import com.jabulani.ligiopen.model.match.entity.dto.matchLocationDto.MatchLocationDto;
import com.jabulani.ligiopen.model.match.entity.dto.matchLocationDto.MatchLocationUpdateDto;
import com.jabulani.ligiopen.model.match.entity.dto.matchLocationDto.mapper.MatchLocationDtoMapper;
import com.jabulani.ligiopen.model.match.entity.dto.postMatchDto.MatchCommentaryCreationDto;
import com.jabulani.ligiopen.model.match.entity.dto.postMatchDto.MatchCommentaryDto;
import com.jabulani.ligiopen.model.match.entity.dto.postMatchDto.MatchCommentaryUpdateDto;
import com.jabulani.ligiopen.model.match.entity.dto.postMatchDto.PostMatchAnalysisDto;
import com.jabulani.ligiopen.model.match.entity.dto.postMatchDto.mapper.MatchCommentaryDtoMapper;
import com.jabulani.ligiopen.model.match.entity.dto.postMatchDto.mapper.PostMatchAnalysisDtoMapper;
import com.jabulani.ligiopen.service.aws.AwsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchServiceImpl implements MatchService{
    private final String BUCKET_NAME = "ligiopen";
    private final AwsService awsService;
    private final MatchDao matchDao;
    private final ClubDao clubDao;
    private final FileDao fileDao;
    private final PlayerDao playerDao;

    private final MatchLocationDtoMapper matchLocationDtoMapper;
    private final MatchFixtureDtoMapper matchFixtureDtoMapper;
    private final MatchCommentaryDtoMapper matchCommentaryDtoMapper;
    private final PostMatchAnalysisDtoMapper postMatchAnalysisDtoMapper;
    @Autowired
    public MatchServiceImpl(
            AwsService awsService,
            MatchDao matchDao,
            ClubDao clubDao,
            MatchLocationDtoMapper matchLocationDtoMapper,
            MatchFixtureDtoMapper matchFixtureDtoMapper,
            FileDao fileDao,
            PlayerDao playerDao,
            MatchCommentaryDtoMapper matchCommentaryDtoMapper,
            PostMatchAnalysisDtoMapper postMatchAnalysisDtoMapper
    ) {
        this.awsService = awsService;
        this.matchDao = matchDao;
        this.clubDao = clubDao;
        this.matchLocationDtoMapper = matchLocationDtoMapper;
        this.matchFixtureDtoMapper = matchFixtureDtoMapper;
        this.fileDao = fileDao;
        this.playerDao = playerDao;
        this.matchCommentaryDtoMapper = matchCommentaryDtoMapper;
        this.postMatchAnalysisDtoMapper = postMatchAnalysisDtoMapper;
    }
    @Transactional
    @Override
    public MatchLocationDto createMatchLocation(MatchLocationCreationDto matchLocationCreationDto) {
        MatchLocation matchLocation = MatchLocation.builder()
                .venueName(matchLocationCreationDto.getVenueName())
                .country(matchLocationCreationDto.getCountry())
                .county(matchLocationCreationDto.getCounty())
                .town(matchLocationCreationDto.getTown())
                .locationPhotos(new ArrayList<>())
                .matchFixtures(new ArrayList<>())
                .build();

        return matchLocationDtoMapper.matchLocationDto(matchDao.createMatchLocation(matchLocation));
    }
    @Transactional
    @Override
    public MatchLocationDto updateMatchLocation(MatchLocationUpdateDto matchLocationUpdateDto) {
        MatchLocation matchLocation = matchDao.getMatchLocationById(matchLocationUpdateDto.getLocationId());

        if(!matchLocationUpdateDto.getVenueName().equals(matchLocation.getVenueName())) {
            matchLocation.setVenueName(matchLocationUpdateDto.getVenueName());
        }

        if(!matchLocationUpdateDto.getCountry().equals(matchLocation.getCountry())) {
            matchLocation.setCountry(matchLocationUpdateDto.getCountry());
        }

        if(!matchLocationUpdateDto.getCounty().equals(matchLocation.getCounty())) {
            matchLocation.setCounty(matchLocationUpdateDto.getCounty());
        }

        if(!matchLocationUpdateDto.getTown().equals(matchLocation.getTown())) {
            matchLocation.setTown(matchLocationUpdateDto.getTown());
        }

        return matchLocationDtoMapper.matchLocationDto(matchDao.updateMatchLocation(matchLocation));
    }
    @Transactional
    @Override
    public MatchLocationDto uploadMatchLocationFiles(Integer locationId, MultipartFile[] files) throws IOException {
        MatchLocation matchLocation = matchDao.getMatchLocationById(locationId);

        for (MultipartFile file : files) {
            File locationFile = File.builder()
                    .matchLocation(matchLocation)
                    .name(awsService.uploadFile(BUCKET_NAME, file))
                    .build();

            matchLocation.getLocationPhotos().add(locationFile);
        }



        return matchLocationDtoMapper.matchLocationDto(matchDao.updateMatchLocation(matchLocation));
    }
    @Transactional
    @Override
    public MatchLocationDto removeMatchLocationFile(Integer locationId, Integer fileId) {
        MatchLocation matchLocation = matchDao.getMatchLocationById(locationId);
        File file = fileDao.getFileById(fileId);
        awsService.deleteFile(BUCKET_NAME, file.getName());
        fileDao.deleteFile(fileId);

        return matchLocationDtoMapper.matchLocationDto(matchDao.updateMatchLocation(matchLocation));
    }

    @Override
    public MatchLocationDto getMatchLocationById(Integer locationId) {

        return matchLocationDtoMapper.matchLocationDto(matchDao.getMatchLocationById(locationId));
    }

    @Override
    public MatchLocationDto getMatchLocationByName(String name) {
        return matchLocationDtoMapper.matchLocationDto(matchDao.getMatchLocationByName(name));
    }

    @Override
    public List<MatchLocationDto> getAllMatchLocations(String venueName, String locationName) {
        return matchDao.getMatchLocations(venueName, locationName).stream().map(matchLocationDtoMapper::matchLocationDto).collect(Collectors.toList());
    }
    @Transactional
    @Override
    public MatchFixtureDto createMatchFixture(MatchFixtureCreationDto matchFixtureCreationDto) {
        MatchLocation matchLocation = matchDao.getMatchLocationById(matchFixtureCreationDto.getLocationId());
        Club homeClub = clubDao.getClubById(matchFixtureCreationDto.getHomeClub());
        Club awayClub = clubDao.getClubById(matchFixtureCreationDto.getAwayClub());

        MatchFixture matchFixture = MatchFixture.builder()
                .matchLocation(matchLocation)
                .homeClub(homeClub)
                .awayClub(awayClub)
                .createdAt(LocalDateTime.now())
                .matchDateTime(matchFixtureCreationDto.getMatchDateTime())
                .status(MatchStatus.PENDING)
                .matchStatistics(new ArrayList<>())
                .build();

        PostMatchAnalysis postMatchAnalysis = PostMatchAnalysis.builder()
                .matchFixture(matchFixture)
                .awayClubScore(0)
                .homeClubScore(0)
                .postMatchAnalysisStatus(PostMatchAnalysisStatus.PENDING)
                .build();

        matchFixture.setPostMatchAnalysis(postMatchAnalysis);

        return matchFixtureDtoMapper.matchFixtureDto(matchDao.createMatchFixture(matchFixture));
    }
    @Transactional
    @Override
    public MatchFixtureDto updateMatchFixture(MatchFixtureUpdateDto matchFixtureUpdateDto) {
        MatchFixture matchFixture = matchDao.getMatchFixtureById(matchFixtureUpdateDto.getMatchFixtureId());

        if(!matchFixtureUpdateDto.getHomeClub().equals(matchFixture.getHomeClub().getId())) {
            Club homeClub = clubDao.getClubById(matchFixture.getHomeClub().getId());
            matchFixture.setHomeClub(homeClub);
        }

        if(!matchFixtureUpdateDto.getAwayClub().equals(matchFixture.getAwayClub().getId())) {
            Club awayClub = clubDao.getClubById(matchFixture.getAwayClub().getId());
            matchFixture.setHomeClub(awayClub);
        }

        if(!matchFixtureUpdateDto.getMatchDateTime().equals(matchFixture.getMatchDateTime())) {
            matchFixture.setMatchDateTime(matchFixtureUpdateDto.getMatchDateTime());
        }

        if(!matchFixtureUpdateDto.getMatchStatus().equals(matchFixture.getStatus())) {
            matchFixture.setStatus(matchFixtureUpdateDto.getMatchStatus());
        }

        return matchFixtureDtoMapper.matchFixtureDto(matchDao.updateMatchFixture(matchFixture));
    }
    @Transactional
    @Override
    public MatchFixtureDto updateMatchFixtureStatus(MatchFixtureStatusUpdateDto matchFixtureStatusUpdateDto) {
        MatchFixture matchFixture = matchDao.getMatchFixtureById(matchFixtureStatusUpdateDto.getMatchFixtureId());
        matchFixture.setStatus(matchFixtureStatusUpdateDto.getMatchStatus());
        return matchFixtureDtoMapper.matchFixtureDto(matchDao.updateMatchFixture(matchFixture));
    }

    @Transactional
    @Override
    public MatchFixtureDto uploadMatchFixtureFiles(Integer fixtureId, MultipartFile[] files) throws IOException {
//        MatchFixture matchFixture = matchDao.getMatchFixtureById(fixtureId);
//
//        for (MultipartFile file : files) {
//            File matchFixtureFile = File.builder()
//                    .name(awsService.uploadFile(BUCKET_NAME, file))
//                    .build();
//
//            matchFixture.get
//        }

        return null;
    }

    @Override
    public MatchFixtureDto removeMatchFixtureFile(Integer fixtureId, Integer fileId) {
        return null;
    }

    @Override
    public MatchFixtureDto getMatchFixtureById(Integer id) {
        return matchFixtureDtoMapper.matchFixtureDto(matchDao.getMatchFixtureById(id));
    }

    @Override
    public List<MatchFixtureDto> getAllMatchFixtures(String status, List<Integer> clubIds, LocalDateTime matchDateTime) {
        return matchDao.getMatchFixtures(status, clubIds, matchDateTime).stream().map(matchFixtureDtoMapper::matchFixtureDto).collect(Collectors.toList());
    }
    @Transactional
    @Override
    public MatchCommentaryDto createMatchCommentary(MatchCommentaryCreationDto matchCommentaryCreationDto) {

        PostMatchAnalysis postMatchAnalysis = matchDao.getPostMatchAnalysisById(matchCommentaryCreationDto.getPostMatchAnalysisId());
        MatchFixture matchFixture = postMatchAnalysis.getMatchFixture();

        Club awayClub = postMatchAnalysis.getMatchFixture().getAwayClub();
        Club homeClub = postMatchAnalysis.getMatchFixture().getHomeClub();

        Player mainPlayer = null;
        Player secondaryPlayer = null;



        Integer awayClubScore = postMatchAnalysis.getAwayClubScore();
        Integer homeClubScore = postMatchAnalysis.getHomeClubScore();



        if (matchCommentaryCreationDto.getMatchEvent().getMainPlayerId() != null) {
            MatchEventType matchEventType = matchCommentaryCreationDto.getMatchEvent().getMatchEventType();
            mainPlayer = playerDao.getPlayerById(matchCommentaryCreationDto.getMatchEvent().getMainPlayerId());

            if(matchEventType.equals(MatchEventType.GOAL)) {
                Club mainPlayerClub = mainPlayer.getPlayerClubs().stream()
                        .filter(playerClub -> !playerClub.getArchived())
                        .map(PlayerClub::getClub).findFirst().orElse(null);

                if(homeClub.equals(mainPlayerClub)) {
                    homeClubScore = homeClubScore + 1;
                } else {
                    awayClubScore = awayClubScore + 1;
                }

                postMatchAnalysis.setAwayClubScore(awayClubScore);
                postMatchAnalysis.setHomeClubScore(homeClubScore);

                matchDao.updatePostMatchAnalysis(postMatchAnalysis);
            } else if(matchEventType.equals(MatchEventType.OWN_GOAL)) {
                Club mainPlayerClub = mainPlayer.getPlayerClubs().stream()
                        .filter(playerClub -> !playerClub.getArchived())
                        .map(PlayerClub::getClub).findFirst().orElse(null);

                if(homeClub.equals(mainPlayerClub)) {
                    awayClubScore = awayClubScore + 1;
                } else {
                    homeClubScore = homeClubScore + 1;
                }

                postMatchAnalysis.setAwayClubScore(awayClubScore);
                postMatchAnalysis.setHomeClubScore(homeClubScore);

                matchDao.updatePostMatchAnalysis(postMatchAnalysis);
            }

        }



        if(matchCommentaryCreationDto.getMatchEvent().getSecondaryPlayerId() != null) {
            secondaryPlayer = playerDao.getPlayerById(matchCommentaryCreationDto.getMatchEvent().getSecondaryPlayerId());
        }

        MatchEvent matchEvent = new MatchEvent();

        switch (matchCommentaryCreationDto.getMatchEvent().getMatchEventType()) {
            case GOAL -> {
                GoalEvent goalEvent = new GoalEvent();
                goalEvent.setTitle(matchCommentaryCreationDto.getMatchEvent().getTitle());
                goalEvent.setSummary(matchCommentaryCreationDto.getMatchEvent().getSummary());
                goalEvent.setMinute(matchCommentaryCreationDto.getMinute());
                goalEvent.setCreatedAt(LocalDateTime.now());
                goalEvent.setPlayer(mainPlayer);
                goalEvent.setAssistingPlayer(secondaryPlayer);
                goalEvent.setMatchEventType(MatchEventType.GOAL);
                matchEvent = goalEvent;
            }

            case OWN_GOAL -> {
                OwnGoalEvent ownGoalEvent = new OwnGoalEvent();
                ownGoalEvent.setTitle(matchCommentaryCreationDto.getMatchEvent().getTitle());
                ownGoalEvent.setSummary(matchCommentaryCreationDto.getMatchEvent().getSummary());
                ownGoalEvent.setMinute(matchCommentaryCreationDto.getMinute());
                ownGoalEvent.setCreatedAt(LocalDateTime.now());
                ownGoalEvent.setPlayer(mainPlayer);
                ownGoalEvent.setMatchEventType(MatchEventType.OWN_GOAL);
                matchEvent = ownGoalEvent;
            }

            case SUBSTITUTION -> {
                mainPlayer.setPlayerState(PlayerState.ACTIVE);
                secondaryPlayer.setPlayerState(PlayerState.BENCH);
                SubstitutionEvent substitutionEvent = new SubstitutionEvent();
                substitutionEvent.setTitle(matchCommentaryCreationDto.getMatchEvent().getTitle());
                substitutionEvent.setSummary(matchCommentaryCreationDto.getMatchEvent().getSummary());
                substitutionEvent.setMinute(matchCommentaryCreationDto.getMinute());
                substitutionEvent.setCreatedAt(LocalDateTime.now());
                substitutionEvent.setPlayer(mainPlayer);
                substitutionEvent.setSubbedOutPlayer(secondaryPlayer);
                substitutionEvent.setMatchEventType(MatchEventType.SUBSTITUTION);
                matchEvent = substitutionEvent;
            }

            case FOUL -> {
                FoulEvent foulEvent = new FoulEvent();
                foulEvent.setTitle(matchCommentaryCreationDto.getMatchEvent().getTitle());
                foulEvent.setSummary(matchCommentaryCreationDto.getMatchEvent().getSummary());
                foulEvent.setMinute(matchCommentaryCreationDto.getMinute());
                foulEvent.setCreatedAt(LocalDateTime.now());
                foulEvent.setPlayer(mainPlayer);
                foulEvent.setFouledPlayer(secondaryPlayer);
                foulEvent.setIsRedCard(matchCommentaryCreationDto.getMatchEvent().getIsRedCard());
                foulEvent.setIsYellowCard(matchCommentaryCreationDto.getMatchEvent().getIsYellowCard());
                foulEvent.setMatchEventType(MatchEventType.FOUL);
                matchEvent = foulEvent;
            }

            case CORNER_KICK -> {
                CornerKickEvent cornerKickEvent = new CornerKickEvent();
                cornerKickEvent.setTitle(matchCommentaryCreationDto.getMatchEvent().getTitle());
                cornerKickEvent.setSummary(matchCommentaryCreationDto.getMatchEvent().getSummary());
                cornerKickEvent.setMinute(matchCommentaryCreationDto.getMinute());
                cornerKickEvent.setCreatedAt(LocalDateTime.now());
                cornerKickEvent.setPlayer(mainPlayer);
                cornerKickEvent.setMatchEventType(MatchEventType.CORNER_KICK);
                matchEvent = cornerKickEvent;
            }

            case FREE_KICK -> {
                FreeKickEvent freeKickEvent = new FreeKickEvent();
                freeKickEvent.setTitle(matchCommentaryCreationDto.getMatchEvent().getTitle());
                freeKickEvent.setSummary(matchCommentaryCreationDto.getMatchEvent().getSummary());
                freeKickEvent.setMinute(matchCommentaryCreationDto.getMinute());
                freeKickEvent.setCreatedAt(LocalDateTime.now());
                freeKickEvent.setPlayer(mainPlayer);
                freeKickEvent.setIsScored(matchCommentaryCreationDto.getMatchEvent().getIsScored());
                freeKickEvent.setMatchEventType(MatchEventType.FREE_KICK);
                matchEvent = freeKickEvent;
            }

            case PENALTY -> {
                PenaltyEvent penaltyEvent = new PenaltyEvent();
                penaltyEvent.setTitle(matchCommentaryCreationDto.getMatchEvent().getTitle());
                penaltyEvent.setSummary(matchCommentaryCreationDto.getMatchEvent().getSummary());
                penaltyEvent.setMinute(matchCommentaryCreationDto.getMinute());
                penaltyEvent.setCreatedAt(LocalDateTime.now());
                penaltyEvent.setPlayer(mainPlayer);
                penaltyEvent.setIsScored(matchCommentaryCreationDto.getMatchEvent().getIsScored());
                penaltyEvent.setMatchEventType(MatchEventType.PENALTY);
                matchEvent = penaltyEvent;
            }

            case INJURY -> {
                InjuryEvent injuryEvent = new InjuryEvent();
                injuryEvent.setTitle(matchCommentaryCreationDto.getMatchEvent().getTitle());
                injuryEvent.setSummary(matchCommentaryCreationDto.getMatchEvent().getSummary());
                injuryEvent.setMinute(matchCommentaryCreationDto.getMinute());
                injuryEvent.setCreatedAt(LocalDateTime.now());
                injuryEvent.setPlayer(mainPlayer);
                injuryEvent.setMatchEventType(MatchEventType.INJURY);
                matchEvent = injuryEvent;
            }

            case THROW_IN -> {
                ThrowInEvent throwInEvent = new ThrowInEvent();
                throwInEvent.setTitle(matchCommentaryCreationDto.getMatchEvent().getTitle());
                throwInEvent.setSummary(matchCommentaryCreationDto.getMatchEvent().getSummary());
                throwInEvent.setMinute(matchCommentaryCreationDto.getMinute());
                throwInEvent.setCreatedAt(LocalDateTime.now());
                throwInEvent.setPlayer(mainPlayer);
                throwInEvent.setMatchEventType(MatchEventType.THROW_IN);
                matchEvent = throwInEvent;
            }

            case KICK_OFF -> {
                KickOffEvent kickOffEvent = new KickOffEvent();
                kickOffEvent.setTitle(matchCommentaryCreationDto.getMatchEvent().getTitle());
                kickOffEvent.setSummary(matchCommentaryCreationDto.getMatchEvent().getSummary());
                kickOffEvent.setMinute(matchCommentaryCreationDto.getMinute());
                kickOffEvent.setCreatedAt(LocalDateTime.now());
                kickOffEvent.setPlayer(mainPlayer);
                kickOffEvent.setMatchEventType(MatchEventType.KICK_OFF);
                if(matchFixture.getStatus().equals(MatchStatus.PENDING)) {
                    postMatchAnalysis.getMatchFixture().setStatus(MatchStatus.PENDING);
                } else if(matchFixture.getStatus().equals(MatchStatus.HALF_TIME)) {
                    postMatchAnalysis.getMatchFixture().setStatus(MatchStatus.SECOND_HALF);
                }
                matchDao.updateMatchFixture(matchFixture);
                matchEvent = kickOffEvent;
            }

            case HALF_TIME -> {
                HalfTimeEvent halfTimeEvent = new HalfTimeEvent();
                halfTimeEvent.setTitle(matchCommentaryCreationDto.getMatchEvent().getTitle());
                halfTimeEvent.setSummary(matchCommentaryCreationDto.getMatchEvent().getSummary());
                halfTimeEvent.setMinute(matchCommentaryCreationDto.getMinute());
                halfTimeEvent.setCreatedAt(LocalDateTime.now());
                halfTimeEvent.setPlayer(mainPlayer);
                halfTimeEvent.setMatchEventType(MatchEventType.HALF_TIME);
                halfTimeEvent.setHomeClubScore(postMatchAnalysis.getHomeClubScore());
                halfTimeEvent.setAwayClubScore(postMatchAnalysis.getAwayClubScore());
                postMatchAnalysis.getMatchFixture().setStatus(MatchStatus.HALF_TIME);
                matchDao.updateMatchFixture(matchFixture);
                matchEvent = halfTimeEvent;
            }

            case FULL_TIME -> {
                FullTimeEvent fullTimeEvent = new FullTimeEvent();
                fullTimeEvent.setTitle(matchCommentaryCreationDto.getMatchEvent().getTitle());
                fullTimeEvent.setSummary(matchCommentaryCreationDto.getMatchEvent().getSummary());
                fullTimeEvent.setMinute(matchCommentaryCreationDto.getMinute());
                fullTimeEvent.setCreatedAt(LocalDateTime.now());
                fullTimeEvent.setPlayer(mainPlayer);
                fullTimeEvent.setMatchEventType(MatchEventType.FULL_TIME);
                fullTimeEvent.setHomeClubScore(postMatchAnalysis.getHomeClubScore());
                fullTimeEvent.setAwayClubScore(postMatchAnalysis.getAwayClubScore());
                postMatchAnalysis.getMatchFixture().setStatus(MatchStatus.COMPLETED);
//                matchFixture.setStatus(MatchStatus.COMPLETED);
                matchDao.updateMatchFixture(matchFixture);

                matchEvent = fullTimeEvent;
            }

            case OFFSIDE -> {
                OffsideEvent offsideEvent = new OffsideEvent();
                offsideEvent.setTitle(matchCommentaryCreationDto.getMatchEvent().getTitle());
                offsideEvent.setSummary(matchCommentaryCreationDto.getMatchEvent().getSummary());
                offsideEvent.setMinute(matchCommentaryCreationDto.getMinute());
                offsideEvent.setCreatedAt(LocalDateTime.now());
                offsideEvent.setPlayer(mainPlayer);
                offsideEvent.setMatchEventType(MatchEventType.OFFSIDE);
                matchEvent = offsideEvent;

            }
        }

        MatchCommentary matchCommentary = MatchCommentary.builder()
                .postMatchAnalysis(postMatchAnalysis)
                .createdAt(LocalDateTime.now())
                .archived(false)
                .matchEvent(matchEvent)
                .minute(matchCommentaryCreationDto.getMinute())
                .build();

        matchEvent.setMatchCommentary(matchCommentary);

        return matchCommentaryDtoMapper.matchCommentaryDto(matchDao.createMatchCommentary(matchCommentary));
    }

    @Transactional
    @Override
    public MatchCommentaryDto updateMatchCommentary(MatchCommentaryUpdateDto matchCommentaryUpdateDto) {
        MatchCommentary matchCommentary = matchDao.getMatchCommentaryById(matchCommentaryUpdateDto.getCommentaryId());
        MatchEvent matchEvent = matchCommentary.getMatchEvent();

        LocalDateTime updatedAt = LocalDateTime.now();

        if(!matchEvent.getTitle().equals(matchCommentaryUpdateDto.getMatchEvent().getTitle())) {
            matchEvent.setTitle(matchCommentaryUpdateDto.getMatchEvent().getTitle());
            matchCommentary.setUpdatedAt(updatedAt);
        }

        if(!matchEvent.getSummary().equals(matchCommentaryUpdateDto.getMatchEvent().getSummary())) {
            matchEvent.setSummary(matchCommentaryUpdateDto.getMatchEvent().getSummary());
            matchCommentary.setUpdatedAt(updatedAt);
        }

        if(!matchEvent.getMinute().equals(matchCommentaryUpdateDto.getMatchEvent().getMinute())) {
            matchEvent.setMinute(matchCommentaryUpdateDto.getMatchEvent().getMinute());
            matchCommentary.setUpdatedAt(updatedAt);
        }

        if(matchEvent.getPlayer() != null) {
            if(!matchEvent.getPlayer().getId().equals(matchCommentaryUpdateDto.getMatchEvent().getMainPlayerId())) {
                Player player = playerDao.getPlayerById(matchCommentaryUpdateDto.getMatchEvent().getMainPlayerId());
                matchEvent.setPlayer(player);
                matchCommentary.setUpdatedAt(updatedAt);
            }
        }



        return matchCommentaryDtoMapper.matchCommentaryDto(matchDao.updateMatchCommentary(matchCommentary));
    }
    @Transactional
    @Override
    public MatchCommentaryDto uploadEventFiles(Integer commentaryId, MultipartFile[] files) throws IOException {

        MatchCommentary matchCommentary = matchDao.getMatchCommentaryById(commentaryId);

        for (MultipartFile file : files) {
            File eventFile = File.builder()
                    .name(awsService.uploadFile(BUCKET_NAME, file))
                    .build();

            matchCommentary.getFiles().add(eventFile);
            eventFile.setMatchCommentary(matchCommentary);
        }
        return matchCommentaryDtoMapper.matchCommentaryDto(matchDao.updateMatchCommentary(matchCommentary));
    }

    @Override
    public MatchCommentaryDto getMatchCommentary(Integer commentaryId) {
        return matchCommentaryDtoMapper.matchCommentaryDto(matchDao.getMatchCommentaryById(commentaryId));
    }

    @Override
    public List<MatchCommentaryDto> getAllMatchCommentaries() {
        return matchDao.getAllMatchCommentaries().stream().map(matchCommentaryDtoMapper::matchCommentaryDto).collect(Collectors.toList());
    }

    @Override
    public PostMatchAnalysisDto getPostMatchDetails(Integer postMatchAnalysisId) {
        return postMatchAnalysisDtoMapper.postMatchAnalysisDto(matchDao.getPostMatchAnalysisById(postMatchAnalysisId));
    }

    @Transactional
    @Override
    public PostMatchAnalysisDto updatePostMatchAnalysisStatus(PostMatchAnalysisStatusUpdateDto postMatchAnalysisStatusUpdateDto) {
        PostMatchAnalysis postMatchAnalysis = matchDao.getPostMatchAnalysisById(postMatchAnalysisStatusUpdateDto.getId());
        postMatchAnalysis.setPostMatchAnalysisStatus(postMatchAnalysisStatusUpdateDto.getPostMatchAnalysisStatus());
        return postMatchAnalysisDtoMapper.postMatchAnalysisDto(matchDao.updatePostMatchAnalysis(postMatchAnalysis));
    }
}
