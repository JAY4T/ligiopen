package com.jabulani.ligiopen.model.match.entity.dto.eventsDto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class FoulEventDto extends MatchEventDto {
    private Integer fouledPlayerId;
    private Boolean isYellowCard = false;
    private Boolean isRedCard = false;
}
