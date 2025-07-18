package com.jabulani.ligiopen.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateClubDto {
    private Integer clubId;
    private Integer divisionId;
    private Integer homeId;
    private String name;
    private String clubAbbreviation;
    private String description;
    private String country;
    private String county;
    private String town;
}
