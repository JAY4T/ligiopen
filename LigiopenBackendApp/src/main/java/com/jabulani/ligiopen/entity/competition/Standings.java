package com.jabulani.ligiopen.entity.competition;

import com.jabulani.ligiopen.entity.club.Club;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "standings")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Standings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    private Season season;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group; // Nullable for league standings

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @Column(name = "matches_played")
    private Integer matchesPlayed = 0;

    @Column(name = "wins")
    private Integer wins = 0;

    @Column(name = "draws")
    private Integer draws = 0;

    @Column(name = "losses")
    private Integer losses = 0;

    @Column(name = "goals_for")
    private Integer goalsFor = 0;

    @Column(name = "goals_against")
    private Integer goalsAgainst = 0;

    @Column(name = "goal_difference")
    private Integer goalDifference = 0;

    @Column(name = "points")
    private Integer points = 0;

    @Column(name = "position")
    private Integer position;

    @Column(name = "form") // Last 5 games: "WWDLW"
    private String form;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @PreUpdate
    @PrePersist
    protected void updateGoalDifference() {
        this.goalDifference = this.goalsFor - this.goalsAgainst;
        this.lastUpdated = LocalDateTime.now();
    }
}