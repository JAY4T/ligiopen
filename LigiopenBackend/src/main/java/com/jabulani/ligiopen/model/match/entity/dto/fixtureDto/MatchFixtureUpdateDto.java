package com.jabulani.ligiopen.model.match.entity.dto.fixtureDto;

import com.jabulani.ligiopen.model.match.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchFixtureUpdateDto {
    private Integer matchFixtureId;
    private Integer homeClub;
    private Integer awayClub;
    private LocalDateTime matchDateTime;
    private MatchStatus matchStatus;
    private String cancellationReason;
    private Integer locationId;
}
