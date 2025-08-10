package com.jabulani.ligiopen.entity.match;

import com.jabulani.ligiopen.entity.club.Club;
import com.jabulani.ligiopen.entity.competition.Group;
import com.jabulani.ligiopen.entity.competition.KnockoutBracket;
import com.jabulani.ligiopen.entity.competition.Season;
import com.jabulani.ligiopen.entity.competition.Stage;
import com.jabulani.ligiopen.entity.location.Stadium;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matches")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    @JoinColumn(name = "knockout_bracket_id")
    private KnockoutBracket knockoutBracket;

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

    @Column(name = "kickoff_time")
    private LocalTime kickoffTime;

    @Column(name = "match_status")
    @Enumerated(EnumType.STRING)
    private MatchStatus matchStatus = MatchStatus.SCHEDULED;

    @Column(name = "status_description")
    private String statusDescription;

    @Column(name = "home_score")
    private Integer homeScore = 0;

    @Column(name = "away_score")
    private Integer awayScore = 0;

    @Column(name = "home_penalties")
    private Integer homePenalties;

    @Column(name = "away_penalties")
    private Integer awayPenalties;

    @Column(name = "attendance")
    private Integer attendance;

    @Column(name = "ticket_price")
    private BigDecimal ticketPrice;

    @Column(name = "referee_name")
    private String refereeName;

    @Column(name = "assistant_referee_1")
    private String assistantReferee1;

    @Column(name = "assistant_referee_2")
    private String assistantReferee2;

    @Column(name = "fourth_official")
    private String fourthOfficial;

    @Column(name = "match_week")
    private Integer matchWeek;

    @Column(name = "leg_number")
    private Integer legNumber; // For two-legged matches

    @Column(name = "extra_time")
    private Boolean extraTime = false;

    @Column(name = "penalty_shootout")
    private Boolean penaltyShootout = false;

    @Column(name = "weather_conditions")
    private String weatherConditions;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "main_photo_id")
    private Long mainPhotoId;

    @Column(name = "match_report", columnDefinition = "TEXT")
    private String matchReport;

    @Column(name = "live_stream_url")
    private String liveStreamUrl;

    @Column(name = "highlight_video_url")
    private String highlightVideoUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    private List<MatchEvent> matchEvents = new ArrayList<>();

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    private List<MatchCommentary> matchCommentaries = new ArrayList<>();

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    private List<MatchLineup> matchLineups = new ArrayList<>();

    public enum MatchStatus {
        SCHEDULED, LIVE, HALF_TIME, FULL_TIME, 
        EXTRA_TIME, PENALTIES, COMPLETED, 
        POSTPONED, CANCELLED, ABANDONED, SUSPENDED
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}