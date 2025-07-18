package com.jabulani.ligiopen.model.dto;

import com.jabulani.ligiopen.model.club.PlayerPosition;
import com.jabulani.ligiopen.model.match.PlayerState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddPlayerDto {
    private String username;
    private Integer age;
    private Double height;
    private Double weight;
    private Integer number;
    private PlayerPosition playerPosition;
    private String country;
    private String county;
    private String town;
    private Integer clubId;
}
