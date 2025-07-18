package com.jabulani.ligiopen.model.match.entity.dto.postMatchDto.mapper;

import com.jabulani.ligiopen.model.aws.dto.FileDto;
import com.jabulani.ligiopen.model.aws.dto.mapper.FileMapper;
import com.jabulani.ligiopen.model.dto.PlayerDto;
import com.jabulani.ligiopen.model.dto.mapper.ClubMapper;
import com.jabulani.ligiopen.model.dto.mapper.PlayerMapper;
import com.jabulani.ligiopen.model.club.entity.Player;
import com.jabulani.ligiopen.model.match.entity.MatchCommentary;
import com.jabulani.ligiopen.model.match.entity.dto.eventsDto.*;
import com.jabulani.ligiopen.model.match.entity.events.*;
import com.jabulani.ligiopen.model.match.entity.dto.eventsDto.mapper.MatchEventsDtoMapper;
import com.jabulani.ligiopen.model.match.entity.dto.postMatchDto.MatchCommentaryDto;
import com.jabulani.ligiopen.service.aws.AwsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MatchCommentaryDtoMapper {
    private final String BUCKET_NAME = "ligiopen";
    private final MatchEventsDtoMapper matchEventsDtoMapper;
    private final FileMapper fileMapper;
    private final PlayerMapper playerMapper;
    private final ClubMapper clubMapper;
    private final AwsService awsService;
    @Autowired
    public MatchCommentaryDtoMapper(
            MatchEventsDtoMapper matchEventsDtoMapper,
            FileMapper fileMapper,
            AwsService awsService,
            PlayerMapper playerMapper,
            ClubMapper clubMapper
    ) {
        this.matchEventsDtoMapper = matchEventsDtoMapper;
        this.fileMapper = fileMapper;
        this.awsService = awsService;
        this.playerMapper = playerMapper;
        this.clubMapper = clubMapper;
    }
    public MatchCommentaryDto matchCommentaryDto(MatchCommentary matchCommentary) {
        MatchEventDto matchEventDto;
        List<FileDto> files = new ArrayList<>();

        Player secondaryPlayer;

        PlayerDto secondaryPlayerDto = null;

        if (matchCommentary.getFiles() != null && !matchCommentary.getFiles().isEmpty()) {
            files.addAll(matchCommentary.getFiles().stream()
                    .map(fileMapper::fileDto)
                    .toList());
        }

        matchEventDto = switch (matchCommentary.getMatchEvent().getMatchEventType()) {
            case GOAL -> matchEventsDtoMapper.goalEventDto((GoalEvent) matchCommentary.getMatchEvent());
            case OWN_GOAL -> matchEventsDtoMapper.ownGoalEventDto((OwnGoalEvent) matchCommentary.getMatchEvent());
            case GOAL_KICK -> matchEventsDtoMapper.goalKickEventDto((GoalKickEvent) matchCommentary.getMatchEvent());
            case SUBSTITUTION -> matchEventsDtoMapper.substitutionEventDto((SubstitutionEvent) matchCommentary.getMatchEvent());
            case FOUL -> matchEventsDtoMapper.foulEventDto((FoulEvent) matchCommentary.getMatchEvent());
            case CORNER_KICK -> matchEventsDtoMapper.cornerKickEventDto((CornerKickEvent) matchCommentary.getMatchEvent());
            case FREE_KICK -> matchEventsDtoMapper.freeKickEventDto((FreeKickEvent) matchCommentary.getMatchEvent());
            case PENALTY -> matchEventsDtoMapper.penaltyEventDto((PenaltyEvent) matchCommentary.getMatchEvent());
            case INJURY -> matchEventsDtoMapper.injuryEventDto((InjuryEvent) matchCommentary.getMatchEvent());
            case THROW_IN -> matchEventsDtoMapper.throwInEventDto((ThrowInEvent) matchCommentary.getMatchEvent());
            case KICK_OFF -> matchEventsDtoMapper.kickOffEventDto((KickOffEvent) matchCommentary.getMatchEvent());
            case HALF_TIME -> matchEventsDtoMapper.halfTimeEventDto((HalfTimeEvent) matchCommentary.getMatchEvent());
            case FULL_TIME -> matchEventsDtoMapper.fullTimeEventDto((FullTimeEvent) matchCommentary.getMatchEvent());
            case OFFSIDE -> matchEventsDtoMapper.offsideEventDto((OffsideEvent) matchCommentary.getMatchEvent());
            default -> null;
        };

        secondaryPlayer = switch (matchCommentary.getMatchEvent().getMatchEventType()) {

            case GOAL -> {
                GoalEvent goalEvent = (GoalEvent) matchCommentary.getMatchEvent();
                yield goalEvent.getAssistingPlayer();
            }
            case OWN_GOAL, YELLOW_CARD, RED_CARD, OFFSIDE, CORNER_KICK, FREE_KICK, PENALTY, PENALTY_MISSED, INJURY, THROW_IN, GOAL_KICK, KICK_OFF, HALF_TIME, FULL_TIME -> null;
            case SUBSTITUTION -> {
                SubstitutionEvent substitutionEvent = (SubstitutionEvent) matchCommentary.getMatchEvent();
                yield substitutionEvent.getSubbedOutPlayer();
            }
            case FOUL -> {
                FoulEvent foulEvent = (FoulEvent) matchCommentary.getMatchEvent();
                yield foulEvent.getFouledPlayer();
            }
        };

        if(secondaryPlayer != null) {
            secondaryPlayerDto = playerMapper.playerDto(secondaryPlayer);
        }


        return MatchCommentaryDto.builder()
                .matchCommentaryId(matchCommentary.getId())
                .postMatchAnalysisId(matchCommentary.getPostMatchAnalysis() != null ? matchCommentary.getPostMatchAnalysis().getId() : null)
                .files(files)
                .minute(matchCommentary.getMinute())
                .createdAt(matchCommentary.getCreatedAt())
                .updatedAt(matchCommentary.getUpdatedAt())
                .archived(matchCommentary.getArchived())
                .archivedAt(matchCommentary.getArchivedAt())
                .matchEventType(matchCommentary.getMatchEvent().getMatchEventType())
                .mainPlayer(matchCommentary.getMatchEvent().getPlayer() != null ? playerMapper.playerDto(matchCommentary.getMatchEvent().getPlayer()) : null)
                .secondaryPlayer(secondaryPlayerDto)
                .homeClub(clubMapper.clubDetailsDto(matchCommentary.getPostMatchAnalysis().getMatchFixture().getHomeClub()))
                .awayClub(clubMapper.clubDetailsDto(matchCommentary.getPostMatchAnalysis().getMatchFixture().getAwayClub()))
                .homeClubScore(matchCommentary.getPostMatchAnalysis().getHomeClubScore())
                .awayClubScore(matchCommentary.getPostMatchAnalysis().getAwayClubScore())
                .cornerEvent(matchEventDto instanceof CornerKickEventDto ? (CornerKickEventDto) matchEventDto : null)
                .foulEvent(matchEventDto instanceof FoulEventDto ? (FoulEventDto) matchEventDto : null)
                .freeKickEvent(matchEventDto instanceof FreeKickEventDto ? (FreeKickEventDto) matchEventDto : null)
                .halfTimeEvent(matchEventDto instanceof HalfTimeEventDto ? (HalfTimeEventDto) matchEventDto : null)
                .fullTimeEvent(matchEventDto instanceof FullTimeEventDto ? (FullTimeEventDto) matchEventDto : null)
                .goalEvent(matchEventDto instanceof GoalEventDto ? (GoalEventDto) matchEventDto : null)
                .goalKickEvent(matchEventDto instanceof GoalKickEventDto ? (GoalKickEventDto) matchEventDto : null)
                .injuryEvent(matchEventDto instanceof InjuryEventDto ? (InjuryEventDto) matchEventDto : null)
                .kickOffEvent(matchEventDto instanceof KickOffEventDto ? (KickOffEventDto) matchEventDto : null)
                .offsideEvent(matchEventDto instanceof OffsideEventDto ? (OffsideEventDto) matchEventDto : null)
                .penaltyEvent(matchEventDto instanceof PenaltyEventDto ? (PenaltyEventDto) matchEventDto : null)
                .ownGoalEvent(matchEventDto instanceof OwnGoalEventDto ? (OwnGoalEventDto) matchEventDto : null)
                .substitutionEvent(matchEventDto instanceof SubstitutionEventDto ? (SubstitutionEventDto) matchEventDto : null)
                .throwInEvent(matchEventDto instanceof ThrowInEventDto ? (ThrowInEventDto) matchEventDto : null)
                .build();
    }


}
