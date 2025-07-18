package com.jabulani.ligiopen.model.match.entity.dto.eventsDto;

import com.jabulani.ligiopen.model.club.entity.Player;
import com.jabulani.ligiopen.model.match.MatchEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchEventCreationDto {
    private Integer matchCommentaryId;
    private String title;
    private String summary;
    private String minute;
    private MatchEventType matchEventType;
    private Integer mainPlayerId;

}
