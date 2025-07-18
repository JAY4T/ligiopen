package com.jabulani.ligiopen.model.match.entity.dto.matchLocationDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchLocationUpdateDto {
    private Integer locationId;
    private String venueName;
    private String country;
    private String county;
    private String town;
}
