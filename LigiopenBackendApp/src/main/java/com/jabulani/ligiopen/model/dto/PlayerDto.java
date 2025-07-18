package com.jabulani.ligiopen.model.dto;

import com.jabulani.ligiopen.model.aws.dto.FileDto;
import com.jabulani.ligiopen.model.club.PlayerPosition;
import com.jabulani.ligiopen.model.match.PlayerState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerDto {
    private Integer playerId;
    private FileDto mainPhoto;
    private String username;
    private Integer number;
    private PlayerPosition playerPosition;
    private Integer age;
    private Double height;
    private Double weight;
    private String country;
    private String county;
    private String town;
    private Integer clubId;
    private List<FileDto> files;
    private PlayerState playerState;
}
