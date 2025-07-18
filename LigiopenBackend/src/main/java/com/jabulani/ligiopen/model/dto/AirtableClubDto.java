package com.jabulani.ligiopen.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AirtableClubDto {
    private String teamName;
    private String teamLogoUrl;
    private String homeStadium;
    private String leagueName;
}
