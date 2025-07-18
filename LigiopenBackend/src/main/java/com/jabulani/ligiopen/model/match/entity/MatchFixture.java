package com.jabulani.ligiopen.model.match.entity;

import com.jabulani.ligiopen.model.club.entity.Club;
import com.jabulani.ligiopen.model.club.entity.LeagueStage;
import com.jabulani.ligiopen.model.club.entity.Player;
import com.jabulani.ligiopen.model.match.MatchStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "match_fixture")
public class MatchFixture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "match_location_id")
    private MatchLocation matchLocation;

    @OneToOne(mappedBy = "matchFixture", cascade = CascadeType.ALL)
    private PostMatchAnalysis postMatchAnalysis;

    @ManyToOne
    @JoinColumn(name = "home_club_id", nullable = false)
    private Club homeClub;

    @ManyToOne
    @JoinColumn(name = "away_club_id", nullable = false)
    private Club awayClub;

    @ManyToOne
    @JoinColumn(name = "winner_club_id", nullable = false)
    private Club winnerClub;

    @ManyToOne
    @JoinColumn(name = "looser_club_id", nullable = false)
    private Club looserClub;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "matchDateTime")
    private LocalDateTime matchDateTime;

    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @OneToMany(mappedBy = "matchFixture", cascade = CascadeType.ALL)
    private List<MatchStatistics> matchStatistics;

    @OneToMany(mappedBy = "matchFixture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchPlayer> matchPlayers;

    // ✅ NEW: formations (e.g. "4-3-3", "3-5-2")
    @Column(name = "home_formation")
    private String homeFormation;

    @Column(name = "away_formation")
    private String awayFormation;

    // ✅ OPTIONAL: team captain per team (useful for rendering or reference)
    @ManyToOne
    @JoinColumn(name = "home_team_captain_id")
    private Player homeTeamCaptain;

    @ManyToOne
    @JoinColumn(name = "away_team_captain_id")
    private Player awayTeamCaptain;

    // ✅ OPTIONAL: referee
    private String referee;

    // ✅ OPTIONAL: match title (e.g., "Semi Final", "Match Day 5")
    private String matchTitle;

    @ManyToOne
    @JoinColumn(name = "league_stage_id")
    private LeagueStage leagueStage;
}

