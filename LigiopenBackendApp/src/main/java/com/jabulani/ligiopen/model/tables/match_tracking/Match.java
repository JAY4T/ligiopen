package com.jabulani.ligiopen.model.tables.match_tracking;

import com.jabulani.ligiopen.model.tables.Stadium;
import com.jabulani.ligiopen.model.tables.competition.Group;
import com.jabulani.ligiopen.model.tables.competition.Season;
import com.jabulani.ligiopen.model.tables.competition.Stage;
import com.jabulani.ligiopen.model.tables.team_and_player.Club;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    private Season season;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id")
    private Stage stage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_club_id")
    private Club homeClub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_club_id")
    private Club awayClub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id")
    private Stadium stadium;

    @Column(name = "match_date")
    private LocalDateTime matchDate;

    @Column(name = "match_status")
    private MatchStatus matchStatus;

    @Column(name = "status_description")
    private String statusDescription;

    @Column(name = "home_score")
    private int homeScore;

    @Column(name = "away_score")
    private int awayScore;

    @Column(name = "main_photo_id")
    private Long mainPhotoId;

    enum MatchStatus {
        SCHEDULED,
        LIVE,
        COMPLETED,
        POSTPONED,
        CANCELLED
    }
}
