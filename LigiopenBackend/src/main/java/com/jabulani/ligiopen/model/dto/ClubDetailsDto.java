package com.jabulani.ligiopen.model.dto;

import com.jabulani.ligiopen.model.aws.dto.FileDto;
import com.jabulani.ligiopen.model.club.entity.ClubStatus;
import com.jabulani.ligiopen.model.match.entity.dto.matchLocationDto.MatchLocationDto;
import com.jabulani.ligiopen.model.user.dto.UserAccountDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubDetailsDto {
    private Integer clubId;
    private FileDto clubLogo;
    private FileDto clubMainPhoto;
    private String name;
    private String clubAbbreviation;
    private String description;
    private String country;
    private String county;
    private String town;
    private LeagueDto division;
    private LocalDate startedOn;
    private LocalDateTime createdAt;
    private Boolean archived;
    private Boolean favorite;
    private LocalDateTime archivedAt;
    private MatchLocationDto home;
    private List<PlayerDto> players;
    private List<FileDto> files;
    private ClubStatus clubStatus;
    private List<UserAccountDto> admins;
}
