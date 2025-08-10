package com.jabulani.ligiopen.entity.statistics;

import com.jabulani.ligiopen.entity.club.Club;
import com.jabulani.ligiopen.entity.match.Match;
import com.jabulani.ligiopen.entity.player.Player;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "match_player_statistics")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchPlayerStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    // Basic Stats
    @Column(name = "minutes_played")
    private Integer minutesPlayed = 0;

    @Column(name = "goals")
    private Integer goals = 0;

    @Column(name = "assists")
    private Integer assists = 0;

    @Column(name = "yellow_cards")
    private Integer yellowCards = 0;

    @Column(name = "red_cards")
    private Integer redCards = 0;

    // Advanced Stats
    @Column(name = "shots")
    private Integer shots = 0;

    @Column(name = "shots_on_target")
    private Integer shotsOnTarget = 0;

    @Column(name = "passes_attempted")
    private Integer passesAttempted = 0;

    @Column(name = "passes_completed")
    private Integer passesCompleted = 0;

    @Column(name = "tackles")
    private Integer tackles = 0;

    @Column(name = "interceptions")
    private Integer interceptions = 0;

    @Column(name = "fouls_committed")
    private Integer foulsCommitted = 0;

    @Column(name = "fouls_suffered")
    private Integer foulsSuffered = 0;

    @Column(name = "rating")
    private Double rating; // Match rating out of 10

    @Column(name = "distance_covered_km")
    private Double distanceCoveredKm;

    @Column(name = "sprints")
    private Integer sprints = 0;
}