package com.jabulani.ligiopen.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddClubDto {
    private String name;
    private String clubAbbreviation;
    private Integer divisionId;
    private Integer homeId;
    private String description;
    private LocalDate startedOn;
    private String country;
    private String county;
    private String town;
}
