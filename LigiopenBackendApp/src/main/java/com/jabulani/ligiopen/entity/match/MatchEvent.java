package com.jabulani.ligiopen.entity.match;

import com.jabulani.ligiopen.entity.club.Club;
import com.jabulani.ligiopen.entity.player.Player;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "match_events")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    private MatchEventType eventType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_player_id")
    private Player primaryPlayer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "secondary_player_id")
    private Player secondaryPlayer; // For assists, substitutions

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tertiary_player_id")
    private Player tertiaryPlayer; // For second assists

    @Column(name = "minute")
    private Integer minute;

    @Column(name = "added_time")
    private Integer addedTime;

    @Column(name = "event_description")
    private String eventDescription;

    @Column(name = "coordinate_x")
    private Double coordinateX; // Field position

    @Column(name = "coordinate_y")
    private Double coordinateY; // Field position

    @Column(name = "metadata", columnDefinition = "JSON")
    private String metadata; // Additional event details

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "is_key_event")
    private Boolean isKeyEvent = false;

    public enum MatchEventType {
        // Goal-Related Events
        GOAL, OWN_GOAL, PENALTY_GOAL, FREE_KICK_GOAL, HEADED_GOAL,
        LONG_RANGE_GOAL, TAP_IN, ASSIST, SECOND_ASSIST, PENALTY_EARNED,

        // Discipline Events
        YELLOW_CARD, RED_CARD, SECOND_YELLOW, STRAIGHT_RED,
        BENCH_YELLOW, BENCH_RED,

        // Substitution Events
        SUBSTITUTION_ON, SUBSTITUTION_OFF, TACTICAL_SUB, INJURY_SUB,

        // Match Administration
        KICKOFF, HALFTIME, FULLTIME, EXTRA_TIME_START,
        PENALTY_SHOOTOUT_START, MATCH_ABANDONED, MATCH_POSTPONED,

        // Set Pieces
        CORNER_KICK, FREE_KICK, THROW_IN, GOAL_KICK,
        PENALTY_AWARDED, PENALTY_SAVED, PENALTY_MISSED,

        // Defensive Events
        SAVE, GOAL_LINE_CLEARANCE, BLOCK, INTERCEPTION,
        TACKLE, FOUL_COMMITTED, OFFSIDE,

        // Miscellaneous Events
        SHOT_ON_TARGET, SHOT_OFF_TARGET, SHOT_HIT_POST,
        BIG_CHANCE_MISSED, INJURY_STOPPAGE, VAR_REVIEW
    }

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}