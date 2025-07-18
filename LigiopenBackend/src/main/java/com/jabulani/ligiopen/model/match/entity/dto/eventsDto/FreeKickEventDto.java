package com.jabulani.ligiopen.model.match.entity.dto.eventsDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@AllArgsConstructor
@NoArgsConstructor
public class FreeKickEventDto extends MatchEventDto{
    private Boolean isScored;
}
