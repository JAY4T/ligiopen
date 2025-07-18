package com.jabulani.ligiopen.model.match.entity.dto.matchLocationDto;

import com.jabulani.ligiopen.model.aws.dto.FileDto;
import com.jabulani.ligiopen.model.dto.ClubDetailsDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MatchLocationDtoMax {
    private Integer locationId;
    private String venueName;
    private String country;
    private String county;
    private String town;
    private List<FileDto> photos;
    private List<Integer> matchFixturesIds;
    private List<ClubDetailsDto> clubs;
}
