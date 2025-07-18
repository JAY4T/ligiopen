package com.jabulani.ligiopen.model.match.entity.dto.postMatchDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostMatchAnalysisDto {
    private Integer id;
    private Integer marchFixtureId;
    private Integer homeClubScore;
    private Integer awayClubScore;
    private Integer manOfTheMatchId;
    private List<MatchCommentaryDto> minuteByMinuteCommentary;
}
