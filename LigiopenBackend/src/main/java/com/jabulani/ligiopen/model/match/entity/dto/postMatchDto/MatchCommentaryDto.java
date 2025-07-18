package com.jabulani.ligiopen.model.match.entity.dto.postMatchDto;

import com.jabulani.ligiopen.model.aws.dto.FileDto;
import com.jabulani.ligiopen.model.dto.ClubDetailsDto;
import com.jabulani.ligiopen.model.dto.PlayerDto;
import com.jabulani.ligiopen.model.match.MatchEventType;
import com.jabulani.ligiopen.model.match.entity.dto.eventsDto.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchCommentaryDto {
    private Integer matchCommentaryId;
    private Integer postMatchAnalysisId;
    private List<FileDto> files;
    private String minute;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean archived;
    private LocalDateTime archivedAt;
    private MatchEventType matchEventType;
    private PlayerDto mainPlayer;
    private PlayerDto secondaryPlayer;
    private ClubDetailsDto homeClub;
    private ClubDetailsDto awayClub;
    private Integer homeClubScore;
    private Integer awayClubScore;
    private CornerKickEventDto cornerEvent;
    private FoulEventDto foulEvent;
    private FreeKickEventDto freeKickEvent;
    private FullTimeEventDto fullTimeEvent;
    private HalfTimeEventDto halfTimeEvent;
    private GoalEventDto goalEvent;
    private GoalKickEventDto goalKickEvent;
    private OwnGoalEventDto ownGoalEvent;
    private InjuryEventDto injuryEvent;
    private KickOffEventDto kickOffEvent;
    private OffsideEventDto offsideEvent;
    private PenaltyEventDto penaltyEvent;
    private SubstitutionEventDto substitutionEvent;
    private ThrowInEventDto throwInEvent;
}
