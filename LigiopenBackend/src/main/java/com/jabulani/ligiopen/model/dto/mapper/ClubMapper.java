package com.jabulani.ligiopen.model.dto.mapper;

import com.jabulani.ligiopen.model.aws.dto.FileDto;
import com.jabulani.ligiopen.model.aws.dto.mapper.FileMapper;
import com.jabulani.ligiopen.model.club.entity.Club;
import com.jabulani.ligiopen.model.club.entity.PlayerClub;
import com.jabulani.ligiopen.model.dto.*;
import com.jabulani.ligiopen.model.user.dto.UserAccountDto;
import com.jabulani.ligiopen.model.user.dto.mapper.UserAccountMapper;
import com.jabulani.ligiopen.model.user.entity.UserAccount;
import com.jabulani.ligiopen.service.aws.AwsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClubMapper {
    private final String BUCKET_NAME = "ligiopen";
    private final PlayerMapper playerMapper;
    private final UserAccountMapper userAccountMapper;
    private final FileMapper fileMapper;
    private final AwsService awsService;
    private final LeagueMapper leagueMapper;

    @Autowired
    public ClubMapper(
            PlayerMapper playerMapper,
            UserAccountMapper userAccountMapper,
            AwsService awsService,
            FileMapper fileMapper,
            LeagueMapper leagueMapper
    ) {
        this.playerMapper = playerMapper;
        this.userAccountMapper = userAccountMapper;
        this.awsService = awsService;
        this.fileMapper = fileMapper;
        this.leagueMapper = leagueMapper;
    }

    public ClubDetailsDto clubDetailsDto(Club club) {

        FileDto clubMainPhoto = null;

        FileDto clubLogo = null;

        List<UserAccountDto> admins = new ArrayList<>();

        if(!club.getManagers().isEmpty()) {
            for(UserAccount userAccount : club.getManagers()) {
                admins.add(userAccountMapper.toUserDto(userAccount));
            }
        }

        if(club.getClubMainPhoto() != null) {
            clubMainPhoto = fileMapper.fileDto(club.getClubMainPhoto());
        }

        if(club.getClubLogo() != null) {
            clubLogo = fileMapper.fileDto(club.getClubLogo());
        }

        List<PlayerDto> players = new ArrayList<>();

        List<FileDto> files = new ArrayList<>();

        if(club.getPlayerClubs() != null) {
            players = club.getPlayerClubs().stream()
                    .filter(playerClub -> !playerClub.getArchived())
                    .map(playerClub -> playerMapper.playerDto(playerClub.getPlayer()))
                    .collect(Collectors.toList());
        }

        if(!club.getFiles().isEmpty()) {
            files.addAll(club.getFiles().stream().map(fileMapper::fileDto).toList());
        }

        return ClubDetailsDto.builder()
                .clubId(club.getId())
                .clubLogo(clubLogo)
                .clubMainPhoto(clubMainPhoto)
                .name(club.getName())
                .division(leagueMapper.leagueDto((club.getLeague())))
                .clubAbbreviation(club.getClubAbbreviation())
                .description(club.getDescription())
                .startedOn(club.getStartedOn())
                .country(club.getCountry())
                .county(club.getCounty())
                .town(club.getTown())
                .favorite(false)
                .createdAt(club.getCreatedAt())
                .archived(club.getArchived())
                .archivedAt(club.getArchivedAt())
                .players(players)
                .files(files)
                .clubStatus(club.getClubStatus())
                .admins(admins)
                .build();
    }

    public ClubDetailsDto clubDetailsDto2(Club club, UserAccount userAccount) {

        FileDto clubMainPhoto = null;

        FileDto clubLogo = null;

        if(club.getClubMainPhoto() != null) {
            clubMainPhoto = fileMapper.fileDto(club.getClubMainPhoto());
        }

        List<UserAccountDto> admins = new ArrayList<>();

        if(!club.getManagers().isEmpty()) {
            for(UserAccount userAcc : club.getManagers()) {
                admins.add(userAccountMapper.toUserDto(userAcc));
            }
        }

        if(club.getClubLogo() != null) {
            clubLogo = fileMapper.fileDto(club.getClubLogo());
        }

        List<PlayerDto> players = new ArrayList<>();

        List<FileDto> files = new ArrayList<>();

        if(club.getPlayerClubs() != null) {
            players = club.getPlayerClubs().stream()
                    .filter(playerClub -> !playerClub.getArchived())
                    .map(playerClub -> playerMapper.playerDto(playerClub.getPlayer()))
                    .collect(Collectors.toList());
        }

        if(!club.getFiles().isEmpty()) {
            files.addAll(club.getFiles().stream().map(fileMapper::fileDto).toList());
        }

        Boolean favorite = false;

        if(club.getBookmarkedBy().contains(userAccount)) {
            favorite = true;
        }

        return ClubDetailsDto.builder()
                .clubId(club.getId())
                .clubLogo(clubLogo)
                .clubMainPhoto(clubMainPhoto)
                .name(club.getName())
                .division(leagueMapper.leagueDto((club.getLeague())))
                .clubAbbreviation(club.getClubAbbreviation())
                .description(club.getDescription())
                .startedOn(club.getStartedOn())
                .country(club.getCountry())
                .county(club.getCounty())
                .town(club.getTown())
                .favorite(favorite)
                .createdAt(club.getCreatedAt())
                .archived(club.getArchived())
                .archivedAt(club.getArchivedAt())
                .players(players)
                .files(files)
                .clubStatus(club.getClubStatus())
                .admins(admins)
                .build();
    }

    public PlayerClubDto playerClubDto(PlayerClub playerClub) {
        return PlayerClubDto.builder()
                .id(playerClub.getId())
                .playerId(playerClub.getPlayer().getId())
                .clubId(playerClub.getClub().getId())
                .createdAt(playerClub.getCreatedAt())
                .archived(playerClub.getArchived())
                .archivedAt(playerClub.getArchivedAt())
                .build();
    }

    public ClubDetailsMin clubDetailsMinDto(Club club) {
        FileDto clubMainPhoto = null;

        FileDto clubLogo = null;

        List<UserAccountDto> admins = new ArrayList<>();

        if(!club.getManagers().isEmpty()) {
            for(UserAccount userAccount : club.getManagers()) {
                admins.add(userAccountMapper.toUserDto(userAccount));
            }
        }

        if(club.getClubMainPhoto() != null) {
            clubMainPhoto = fileMapper.fileDto(club.getClubMainPhoto());
        }

        if(club.getClubLogo() != null) {
            clubLogo = fileMapper.fileDto(club.getClubLogo());
        }

        List<FileDto> files = new ArrayList<>();


        if(!club.getFiles().isEmpty()) {
            files.addAll(club.getFiles().stream().map(fileMapper::fileDto).toList());
        }
        return ClubDetailsMin.builder()
                .clubId(club.getId())
                .clubName(club.getName())
                .leagueName(club.getLeague().getName())
                .clubLogo(clubLogo)
                .clubPhotos(files)
                .clubStatus(club.getClubStatus())
                .admin(admins)
                .build();
    }

    public UserBookmarkedClubsDto userBookmarkedClubDto(UserAccount userAccount) {
        return UserBookmarkedClubsDto.builder()
                .userId(userAccount.getId())
                .username(userAccount.getUsername())
                .clubs(userAccount.getFavoriteClubs().stream().map(this::clubDetailsMinDto).collect(Collectors.toList()))
                .build();
    }


}
