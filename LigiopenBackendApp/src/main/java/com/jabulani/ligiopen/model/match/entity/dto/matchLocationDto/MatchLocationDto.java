package com.jabulani.ligiopen.model.match.entity.dto.matchLocationDto;

import com.jabulani.ligiopen.model.aws.dto.FileDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchLocationDto {
    private Integer locationId;
    private String venueName;
    private String country;
    private String county;
    private String town;
    private List<FileDto> photos;
}
