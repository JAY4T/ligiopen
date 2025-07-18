package com.jabulani.ligiopen.model.club.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "league_standing")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeagueStanding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private League league;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    private Integer played;
    private Integer wins;
    private Integer draws;
    private Integer losses;

    @Column(name = "goals_for")
    private Integer goalsFor;

    @Column(name = "goals_against")
    private Integer goalsAgainst;

    @Column(name = "goal_difference")
    private Integer goalDifference;

    private Integer points;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_stage_id")
    private LeagueStage leagueStage;

    @Column(name = "group_name") // e.g., Group A
    private String groupName;
}
