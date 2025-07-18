package com.jabulani.ligiopen.model.club.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "league_stage_club")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeagueStageClub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "league_stage_id")
    private LeagueStage leagueStage;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;
}
