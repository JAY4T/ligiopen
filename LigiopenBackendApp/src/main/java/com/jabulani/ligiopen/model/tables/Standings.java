package com.jabulani.ligiopen.model.tables;

import com.jabulani.ligiopen.model.tables.competition.Season;
import com.jabulani.ligiopen.model.tables.team_and_player.Club;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "standings")
public class Standings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    private Season season;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @Column(name = "matches_played")
    private int matchesPlayed;

    private int wins;

    private int draws;

    private int losses;

    @Column(name = "goals_for")
    private int goalsFor;

    @Column(name = "goals_against")
    private int goalsAgainst;

    private int points;

    @Column(name = "goal_difference")
    private int goalsDifference;

    private int position;
}
