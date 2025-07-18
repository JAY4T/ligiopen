package com.jabulani.ligiopen.model.match.entity.dto.eventsDto.mapper;

import com.jabulani.ligiopen.model.aws.dto.FileDto;
import com.jabulani.ligiopen.model.aws.dto.mapper.FileMapper;
import com.jabulani.ligiopen.model.match.entity.dto.eventsDto.*;
import com.jabulani.ligiopen.model.match.entity.events.*;
import com.jabulani.ligiopen.service.aws.AwsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MatchEventsDtoMapper {

    private final String BUCKET_NAME = "ligiopen";

    private final AwsService awsService;

    private final FileMapper fileMapper;
    @Autowired
    public MatchEventsDtoMapper(
            AwsService awsService,
            FileMapper fileMapper
    ) {
        this.awsService = awsService;
        this.fileMapper = fileMapper;
    }
    public CornerKickEventDto cornerKickEventDto(CornerKickEvent cornerKickEvent) {
        List<FileDto> files = new ArrayList<>();

        if (cornerKickEvent.getMatchCommentary() != null &&
                cornerKickEvent.getMatchCommentary().getFiles() != null &&
                !cornerKickEvent.getMatchCommentary().getFiles().isEmpty()) {

            files.addAll(cornerKickEvent.getMatchCommentary().getFiles().stream()
                    .map(fileMapper::fileDto)
                    .toList());
        }

        CornerKickEventDto cornerKickEventDto = new CornerKickEventDto();
        cornerKickEventDto.setTitle(cornerKickEvent.getTitle());
        cornerKickEventDto.setSummary(cornerKickEvent.getSummary());
        cornerKickEventDto.setMinute(cornerKickEvent.getMinute());
        cornerKickEventDto.setMatchEventType(cornerKickEvent.getMatchEventType());
        cornerKickEventDto.setMainPlayerId(cornerKickEvent.getPlayer() != null ? cornerKickEvent.getPlayer().getId() : null);
        cornerKickEventDto.setFiles(files);

        return cornerKickEventDto;
    }


    public FoulEventDto foulEventDto(FoulEvent foulEvent) {
        List<FileDto> files = new ArrayList<>();

        if (foulEvent.getMatchCommentary() != null &&
                foulEvent.getMatchCommentary().getFiles() != null &&
                !foulEvent.getMatchCommentary().getFiles().isEmpty()) {

            files.addAll(foulEvent.getMatchCommentary().getFiles().stream()
                    .map(fileMapper::fileDto)
                    .toList());
        }

        FoulEventDto foulEventDto = new FoulEventDto();
        foulEventDto.setTitle(foulEvent.getTitle());
        foulEventDto.setSummary(foulEvent.getSummary());
        foulEventDto.setMinute(foulEvent.getMinute());
        foulEventDto.setMatchEventType(foulEvent.getMatchEventType());
        foulEventDto.setMainPlayerId(foulEvent.getPlayer() != null ? foulEvent.getPlayer().getId() : null);
        foulEventDto.setFouledPlayerId(foulEvent.getFouledPlayer() != null ? foulEvent.getFouledPlayer().getId() : null);
        foulEventDto.setIsYellowCard(foulEvent.getIsYellowCard());
        foulEventDto.setIsRedCard(foulEvent.getIsRedCard());
        foulEventDto.setFiles(files);

        return foulEventDto;
    }


    public FreeKickEventDto freeKickEventDto(FreeKickEvent freeKickEvent) {
        List<FileDto> files = new ArrayList<>();

        if (freeKickEvent.getMatchCommentary() != null &&
                freeKickEvent.getMatchCommentary().getFiles() != null &&
                !freeKickEvent.getMatchCommentary().getFiles().isEmpty()) {

            files.addAll(freeKickEvent.getMatchCommentary().getFiles().stream()
                    .map(fileMapper::fileDto)
                    .toList());
        }

        FreeKickEventDto freeKickEventDto = new FreeKickEventDto();
        freeKickEventDto.setTitle(freeKickEvent.getTitle());
        freeKickEventDto.setSummary(freeKickEvent.getSummary());
        freeKickEventDto.setMinute(freeKickEvent.getMinute());
        freeKickEventDto.setMatchEventType(freeKickEvent.getMatchEventType());
        freeKickEventDto.setIsScored(freeKickEvent.getIsScored());
        freeKickEventDto.setMainPlayerId(freeKickEvent.getPlayer() != null ? freeKickEvent.getPlayer().getId() : null);
        freeKickEventDto.setFiles(files);

        return freeKickEventDto;
    }


    public FullTimeEventDto fullTimeEventDto(FullTimeEvent fullTimeEvent) {
        List<FileDto> files = new ArrayList<>();

        if (fullTimeEvent.getMatchCommentary() != null &&
                fullTimeEvent.getMatchCommentary().getFiles() != null &&
                !fullTimeEvent.getMatchCommentary().getFiles().isEmpty()) {

            files.addAll(fullTimeEvent.getMatchCommentary().getFiles().stream()
                    .map(fileMapper::fileDto)
                    .toList());
        }

        FullTimeEventDto fullTimeEventDto = new FullTimeEventDto();
        fullTimeEventDto.setTitle(fullTimeEvent.getTitle());
        fullTimeEventDto.setSummary(fullTimeEvent.getSummary());
        fullTimeEventDto.setMinute(fullTimeEvent.getMinute());
        fullTimeEventDto.setMatchEventType(fullTimeEvent.getMatchEventType());
        fullTimeEventDto.setMainPlayerId(fullTimeEvent.getPlayer() != null ? fullTimeEvent.getPlayer().getId() : null);
        fullTimeEventDto.setFiles(files);
        fullTimeEventDto.setHomeClubScore(fullTimeEvent.getHomeClubScore());
        fullTimeEventDto.setAwayClubScore(fullTimeEvent.getAwayClubScore());

        return fullTimeEventDto;
    }


    public GoalEventDto goalEventDto(GoalEvent goalEvent) {
        List<FileDto> files = new ArrayList<>();

        if (goalEvent.getMatchCommentary() != null &&
                goalEvent.getMatchCommentary().getFiles() != null &&
                !goalEvent.getMatchCommentary().getFiles().isEmpty()) {

            files.addAll(goalEvent.getMatchCommentary().getFiles().stream()
                    .map(fileMapper::fileDto)
                    .toList());
        }

        GoalEventDto goalEventDto = new GoalEventDto();
        goalEventDto.setTitle(goalEvent.getTitle());
        goalEventDto.setSummary(goalEvent.getSummary());
        goalEventDto.setMinute(goalEvent.getMinute());
        goalEventDto.setMatchEventType(goalEvent.getMatchEventType());
        goalEventDto.setMainPlayerId(goalEvent.getPlayer() != null ? goalEvent.getPlayer().getId() : null);
        goalEventDto.setSecondaryPlayerId(goalEvent.getAssistingPlayer() != null ? goalEvent.getAssistingPlayer().getId() : null);
        goalEventDto.setFiles(files);

        return goalEventDto;
    }


    public InjuryEventDto injuryEventDto(InjuryEvent injuryEvent) {
        List<FileDto> files = new ArrayList<>();

        if (injuryEvent.getMatchCommentary() != null &&
                injuryEvent.getMatchCommentary().getFiles() != null &&
                !injuryEvent.getMatchCommentary().getFiles().isEmpty()) {

            files.addAll(injuryEvent.getMatchCommentary().getFiles().stream()
                    .map(fileMapper::fileDto)
                    .toList());
        }

        InjuryEventDto injuryEventDto = new InjuryEventDto();
        injuryEventDto.setTitle(injuryEvent.getTitle());
        injuryEventDto.setSummary(injuryEvent.getSummary());
        injuryEventDto.setMinute(injuryEvent.getMinute());
        injuryEventDto.setMatchEventType(injuryEvent.getMatchEventType());
        injuryEventDto.setMainPlayerId(injuryEvent.getPlayer() != null ? injuryEvent.getPlayer().getId() : null);
        injuryEventDto.setFiles(files);

        return injuryEventDto;
    }


    public KickOffEventDto kickOffEventDto(KickOffEvent kickOffEvent) {
        List<FileDto> files = new ArrayList<>();

        Integer playerId = null;

        if(kickOffEvent.getPlayer() != null) {
            playerId = kickOffEvent.getId();
        }


        if(kickOffEvent.getMatchCommentary().getFiles() != null && !kickOffEvent.getMatchCommentary().getFiles().isEmpty()) {
            files.addAll(kickOffEvent.getMatchCommentary().getFiles().stream().map(fileMapper::fileDto).toList());
        }

        KickOffEventDto kickOffEventDto = new KickOffEventDto();
        kickOffEventDto.setTitle(kickOffEvent.getTitle());
        kickOffEventDto.setSummary(kickOffEvent.getSummary());
        kickOffEventDto.setMinute(kickOffEvent.getMinute());
        kickOffEventDto.setMatchEventType(kickOffEvent.getMatchEventType());
        kickOffEventDto.setMainPlayerId(playerId);
        kickOffEventDto.setFiles(files);

        return kickOffEventDto;
    }

    public HalfTimeEventDto halfTimeEventDto(HalfTimeEvent halfTimeEvent) {
        List<FileDto> files = new ArrayList<>();

        Integer playerId = null;

        if(halfTimeEvent.getPlayer() != null) {
            playerId = halfTimeEvent.getId();
        }


        if(halfTimeEvent.getMatchCommentary().getFiles() != null && !halfTimeEvent.getMatchCommentary().getFiles().isEmpty()) {
            files.addAll(halfTimeEvent.getMatchCommentary().getFiles().stream().map(fileMapper::fileDto).toList());
        }

        HalfTimeEventDto halfTimeEventDto = new HalfTimeEventDto();
        halfTimeEventDto.setTitle(halfTimeEvent.getTitle());
        halfTimeEventDto.setSummary(halfTimeEvent.getSummary());
        halfTimeEventDto.setMinute(halfTimeEvent.getMinute());
        halfTimeEventDto.setMatchEventType(halfTimeEvent.getMatchEventType());
        halfTimeEventDto.setMainPlayerId(playerId);
        halfTimeEventDto.setFiles(files);
        halfTimeEventDto.setHomeClubScore(halfTimeEvent.getHomeClubScore());
        halfTimeEventDto.setAwayClubScore(halfTimeEventDto.getAwayClubScore());

        return halfTimeEventDto;
    }

    public GoalKickEventDto goalKickEventDto(GoalKickEvent goalKickEvent) {
        List<FileDto> files = new ArrayList<>();

        Integer playerId = null;

        if(goalKickEvent.getPlayer() != null) {
            playerId = goalKickEvent.getId();
        }


        if(goalKickEvent.getMatchCommentary().getFiles() != null && !goalKickEvent.getMatchCommentary().getFiles().isEmpty()) {
            files.addAll(goalKickEvent.getMatchCommentary().getFiles().stream().map(fileMapper::fileDto).toList());
        }

        GoalKickEventDto goalKickEventDto = new GoalKickEventDto();
        goalKickEventDto.setTitle(goalKickEvent.getTitle());
        goalKickEventDto.setSummary(goalKickEvent.getSummary());
        goalKickEventDto.setMinute(goalKickEvent.getMinute());
        goalKickEventDto.setMatchEventType(goalKickEvent.getMatchEventType());
        goalKickEventDto.setMainPlayerId(playerId);
        goalKickEventDto.setFiles(files);

        return goalKickEventDto;
    }

    public OffsideEventDto offsideEventDto(OffsideEvent offsideEvent) {
        List<FileDto> files = new ArrayList<>();

        if (offsideEvent.getMatchCommentary() != null &&
                offsideEvent.getMatchCommentary().getFiles() != null &&
                !offsideEvent.getMatchCommentary().getFiles().isEmpty()) {

            files.addAll(offsideEvent.getMatchCommentary().getFiles().stream()
                    .map(fileMapper::fileDto)
                    .toList());
        }

        OffsideEventDto offsideEventDto = new OffsideEventDto();
        offsideEventDto.setTitle(offsideEvent.getTitle());
        offsideEventDto.setSummary(offsideEvent.getSummary());
        offsideEventDto.setMinute(offsideEvent.getMinute());
        offsideEventDto.setMatchEventType(offsideEvent.getMatchEventType());
        offsideEventDto.setMainPlayerId(offsideEvent.getPlayer() != null ? offsideEvent.getPlayer().getId() : null);
        offsideEventDto.setFiles(files);

        return offsideEventDto;
    }


    public PenaltyEventDto penaltyEventDto(PenaltyEvent penaltyEvent) {
        List<FileDto> files = new ArrayList<>();

        if (penaltyEvent.getMatchCommentary() != null &&
                penaltyEvent.getMatchCommentary().getFiles() != null &&
                !penaltyEvent.getMatchCommentary().getFiles().isEmpty()) {

            files.addAll(penaltyEvent.getMatchCommentary().getFiles().stream()
                    .map(fileMapper::fileDto)
                    .toList());
        }

        PenaltyEventDto penaltyEventDto = new PenaltyEventDto();
        penaltyEventDto.setTitle(penaltyEvent.getTitle());
        penaltyEventDto.setSummary(penaltyEvent.getSummary());
        penaltyEventDto.setMinute(penaltyEvent.getMinute());
        penaltyEventDto.setMatchEventType(penaltyEvent.getMatchEventType());
        penaltyEventDto.setMainPlayerId(penaltyEvent.getPlayer() != null ? penaltyEvent.getPlayer().getId() : null);
        penaltyEventDto.setIsScored(penaltyEvent.getIsScored());
        penaltyEventDto.setFiles(files);

        return penaltyEventDto;
    }


    public SubstitutionEventDto substitutionEventDto(SubstitutionEvent substitutionEvent) {
        List<FileDto> files = new ArrayList<>();

        if (substitutionEvent.getMatchCommentary() != null &&
                substitutionEvent.getMatchCommentary().getFiles() != null &&
                !substitutionEvent.getMatchCommentary().getFiles().isEmpty()) {

            files.addAll(substitutionEvent.getMatchCommentary().getFiles().stream()
                    .map(fileMapper::fileDto)
                    .toList());
        }

        SubstitutionEventDto substitutionEventDto = new SubstitutionEventDto();
        substitutionEventDto.setTitle(substitutionEvent.getTitle());
        substitutionEventDto.setSummary(substitutionEvent.getSummary());
        substitutionEventDto.setMinute(substitutionEvent.getMinute());
        substitutionEventDto.setMatchEventType(substitutionEvent.getMatchEventType());
        substitutionEventDto.setMainPlayerId(substitutionEvent.getPlayer() != null ? substitutionEvent.getPlayer().getId() : null);
        substitutionEventDto.setSubbedOutPlayerId(substitutionEvent.getSubbedOutPlayer() != null ? substitutionEvent.getSubbedOutPlayer().getId() : null);
        substitutionEventDto.setFiles(files);

        return substitutionEventDto;
    }


    public ThrowInEventDto throwInEventDto(ThrowInEvent throwInEvent) {
        List<FileDto> files = new ArrayList<>();

        if (throwInEvent.getMatchCommentary() != null &&
                throwInEvent.getMatchCommentary().getFiles() != null &&
                !throwInEvent.getMatchCommentary().getFiles().isEmpty()) {

            files.addAll(throwInEvent.getMatchCommentary().getFiles().stream()
                    .map(fileMapper::fileDto)
                    .toList());
        }

        ThrowInEventDto throwInEventDto = new ThrowInEventDto();
        throwInEventDto.setTitle(throwInEvent.getTitle());
        throwInEventDto.setSummary(throwInEvent.getSummary());
        throwInEventDto.setMinute(throwInEvent.getMinute());
        throwInEventDto.setMatchEventType(throwInEvent.getMatchEventType());
        throwInEventDto.setMainPlayerId(throwInEvent.getPlayer() != null ? throwInEvent.getPlayer().getId() : null);
        throwInEventDto.setFiles(files);

        return throwInEventDto;
    }


    public OwnGoalEventDto ownGoalEventDto(OwnGoalEvent ownGoalEvent) {
        List<FileDto> files = new ArrayList<>();

        if (ownGoalEvent.getMatchCommentary() != null &&
                ownGoalEvent.getMatchCommentary().getFiles() != null &&
                !ownGoalEvent.getMatchCommentary().getFiles().isEmpty()) {

            files.addAll(ownGoalEvent.getMatchCommentary().getFiles().stream()
                    .map(fileMapper::fileDto)
                    .toList());
        }

        OwnGoalEventDto ownGoalEventDto = new OwnGoalEventDto();
        ownGoalEventDto.setTitle(ownGoalEvent.getTitle());
        ownGoalEventDto.setSummary(ownGoalEvent.getSummary());
        ownGoalEventDto.setMinute(ownGoalEvent.getMinute());
        ownGoalEventDto.setMatchEventType(ownGoalEvent.getMatchEventType());
        ownGoalEventDto.setMainPlayerId(ownGoalEvent.getPlayer() != null ? ownGoalEvent.getPlayer().getId() : null);
        ownGoalEventDto.setFiles(files);

        return ownGoalEventDto;
    }

}
