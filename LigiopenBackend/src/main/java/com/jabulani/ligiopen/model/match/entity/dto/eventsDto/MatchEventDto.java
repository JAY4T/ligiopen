package com.jabulani.ligiopen.model.match.entity.dto.eventsDto;

import com.jabulani.ligiopen.model.aws.dto.FileDto;
import com.jabulani.ligiopen.model.match.MatchEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
//@Builder
public class MatchEventDto {
    private String title;
    private String summary;
    private String minute;
    private MatchEventType matchEventType;
    private Integer mainPlayerId;
    private Integer secondaryPlayerId;
    private Integer fouledPlayerId;
    private Boolean isYellowCard;
    private Boolean isRedCard;
    private Boolean isScored;
    private List<FileDto> files;
}
