package com.jabulani.ligiopen.model.match.entity.dto.fixtureDto;

import com.jabulani.ligiopen.model.match.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchFixtureStatusUpdateDto {
    private Integer matchFixtureId;
    private MatchStatus matchStatus;
}
