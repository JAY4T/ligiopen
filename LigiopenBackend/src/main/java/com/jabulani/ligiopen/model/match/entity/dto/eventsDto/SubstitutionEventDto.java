package com.jabulani.ligiopen.model.match.entity.dto.eventsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
//@Builder
public class SubstitutionEventDto extends MatchEventDto {
    private Integer subbedOutPlayerId;
}
