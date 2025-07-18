package com.jabulani.ligiopen.model.match.entity.dto.postMatchDto.mapper;

import com.jabulani.ligiopen.model.match.entity.PostMatchAnalysis;
import com.jabulani.ligiopen.model.match.entity.dto.postMatchDto.PostMatchAnalysisDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PostMatchAnalysisDtoMapper {
    private final MatchCommentaryDtoMapper matchCommentaryDtoMapper;
    @Autowired
    public PostMatchAnalysisDtoMapper(
            MatchCommentaryDtoMapper matchCommentaryDtoMapper
    ) {
        this.matchCommentaryDtoMapper = matchCommentaryDtoMapper;
    }

    public PostMatchAnalysisDto postMatchAnalysisDto(PostMatchAnalysis postMatchAnalysis) {

        Integer manOfTheMatchId = null;

        if(postMatchAnalysis.getManOfTheMatch() != null) {
            manOfTheMatchId = postMatchAnalysis.getManOfTheMatch().getId();
        }

        return PostMatchAnalysisDto.builder()
                .id(postMatchAnalysis.getId())
                .marchFixtureId(postMatchAnalysis.getMatchFixture().getId())
                .homeClubScore(postMatchAnalysis.getHomeClubScore())
                .awayClubScore(postMatchAnalysis.getAwayClubScore())
                .manOfTheMatchId(manOfTheMatchId)
                .minuteByMinuteCommentary(postMatchAnalysis.getMinuteByMinuteCommentary().stream().map(matchCommentaryDtoMapper::matchCommentaryDto).collect(Collectors.toList()))
                .build();
    }
}
