package com.jabulani.ligiopen.model.match.entity.dto.eventsDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@AllArgsConstructor
@NoArgsConstructor
public class FullTimeEventDto extends MatchEventDto{
    private Integer awayClubScore;
    private Integer homeClubScore;
}
