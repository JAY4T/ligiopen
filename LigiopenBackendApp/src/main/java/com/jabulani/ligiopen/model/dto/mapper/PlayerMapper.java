package com.jabulani.ligiopen.model.dto.mapper;

import com.jabulani.ligiopen.model.aws.dto.FileDto;
import com.jabulani.ligiopen.model.aws.dto.mapper.FileMapper;
import com.jabulani.ligiopen.model.club.entity.Player;
import com.jabulani.ligiopen.model.dto.PlayerDto;
import com.jabulani.ligiopen.service.aws.AwsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlayerMapper {

    private final String BUCKET_NAME = "ligiopen";

    private final FileMapper fileMapper;

    private final AwsService awsService;
    @Autowired
    public PlayerMapper(
            AwsService awsService,
            FileMapper fileMapper
    ) {
        this.awsService = awsService;
        this.fileMapper = fileMapper;
    }
    public PlayerDto playerDto(Player player) {

        Integer clubId = null;

        if(player.getPlayerClubs() != null) {
            clubId = player.getPlayerClubs().stream()
                    .filter(playerClub -> !playerClub.getArchived())
                    .map(playerClub -> playerClub.getClub().getId())
                    .findFirst()
                    .orElse(null);
        }

        List<FileDto> files = new ArrayList<>();

        FileDto playerMainPhoto = null;

        if(player.getMainPhoto() != null) {
            playerMainPhoto = fileMapper.fileDto(player.getMainPhoto());
        }

        if(!player.getFiles().isEmpty()) {
            files.addAll(player.getFiles().stream().map(fileMapper::fileDto).toList());
        }

        return PlayerDto.builder()
                .playerId(player.getId())
                .mainPhoto(playerMainPhoto)
                .username(player.getUsername())
                .number(player.getNumber())
                .playerPosition(player.getPlayerPosition())
                .playerState(player.getPlayerState())
                .age(player.getAge())
                .height(player.getHeight())
                .weight(player.getWeight())
                .country(player.getCountry())
                .county(player.getCounty())
                .town(player.getTown())
                .clubId(clubId)
                .files(files)
                .build();
    }
}
