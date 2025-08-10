package com.jabulani.ligiopen.entity.statistics;

import com.jabulani.ligiopen.entity.club.Club;
import com.jabulani.ligiopen.entity.competition.Season;
import com.jabulani.ligiopen.entity.player.Player;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "player_statistics")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    private Season season;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    // Appearance Stats
    @Column(name = "appearances")
    private Integer appearances = 0;

    @Column(name = "starts")
    private Integer starts = 0;

    @Column(name = "minutes_played")
    private Integer minutesPlayed = 0;

    // Goal Stats
    @Column(name = "goals")
    private Integer goals = 0;

    @Column(name = "assists")
    private Integer assists = 0;

    @Column(name = "penalties_scored")
    private Integer penaltiesScored = 0;

    @Column(name = "penalties_missed")
    private Integer penaltiesMissed = 0;

    @Column(name = "free_kick_goals")
    private Integer freeKickGoals = 0;

    // Discipline Stats
    @Column(name = "yellow_cards")
    private Integer yellowCards = 0;

    @Column(name = "red_cards")
    private Integer redCards = 0;

    // Shooting Stats
    @Column(name = "shots")
    private Integer shots = 0;

    @Column(name = "shots_on_target")
    private Integer shotsOnTarget = 0;

    @Column(name = "shot_accuracy")
    private Double shotAccuracy = 0.0;

    // Passing Stats
    @Column(name = "passes_attempted")
    private Integer passesAttempted = 0;

    @Column(name = "passes_completed")
    private Integer passesCompleted = 0;

    @Column(name = "pass_accuracy")
    private Double passAccuracy = 0.0;

    // Defensive Stats
    @Column(name = "tackles")
    private Integer tackles = 0;

    @Column(name = "interceptions")
    private Integer interceptions = 0;

    @Column(name = "clearances")
    private Integer clearances = 0;

    @Column(name = "blocks")
    private Integer blocks = 0;

    // Goalkeeper Stats
    @Column(name = "saves")
    private Integer saves = 0;

    @Column(name = "clean_sheets")
    private Integer cleanSheets = 0;

    @Column(name = "goals_conceded")
    private Integer goalsConceded = 0;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @PreUpdate
    @PrePersist
    protected void updateTimestamp() {
        lastUpdated = LocalDateTime.now();
        // Calculate derived stats
        if (passesAttempted > 0) {
            passAccuracy = (double) passesCompleted / passesAttempted * 100;
        }
        if (shots > 0) {
            shotAccuracy = (double) shotsOnTarget / shots * 100;
        }
    }
}