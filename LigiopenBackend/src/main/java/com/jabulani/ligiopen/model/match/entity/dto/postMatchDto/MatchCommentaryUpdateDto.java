package com.jabulani.ligiopen.model.match.entity.dto.postMatchDto;

import com.jabulani.ligiopen.model.match.entity.dto.eventsDto.MatchEventDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchCommentaryUpdateDto {
    private Integer commentaryId;
    private Integer postMatchAnalysisId;
    private String minute;
    private MatchEventDto matchEvent;
}
