package com.jabulani.ligiopen.model.tables.match_tracking;

import com.jabulani.ligiopen.model.tables.team_and_player.Player;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "match_events")
public class MatchEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @Enumerated(EnumType.STRING)
    @Column(name = "match_event_type")
    private MatchEventType eventType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_player_id")
    private Player primaryPlayer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "secondary_player_id")
    private Player secondaryPlayer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tertiary_player_id")
    private Player tertiaryPlayer;

    private Integer minute;

    @Column(name = "added_time")
    private Integer addedTime;

    @Column(columnDefinition = "JSON")
    private String metadata; // For additional event details

    private LocalDateTime timestamp;

    public enum MatchEventType {
        // Goal-Related Events
        GOAL,
        OWN_GOAL,
        PENALTY_GOAL,
        FREE_KICK_GOAL,
        HEADED_GOAL,
        LONG_RANGE_GOAL,
        TAP_IN,
        ASSIST,
        SECOND_ASSIST,
        PENALTY_EARNED,

        // Discipline Events
        YELLOW_CARD,
        RED_CARD,
        SECOND_YELLOW,
        STRAIGHT_RED,
        BENCH_YELLOW,
        BENCH_RED,

        // Substitution Events
        SUBSTITUTION_ON,
        SUBSTITUTION_OFF,
        TACTICAL_SUB,
        INJURY_SUB,
        CONCUSSION_SUB,

        // Match Administration
        KICKOFF,
        HALFTIME,
        FULLTIME,
        EXTRA_TIME_START,
        PENALTY_SHOOTOUT_START,
        MATCH_ABANDONED,
        MATCH_POSTPONED,

        // Set Pieces
        CORNER_KICK,
        FREE_KICK,
        THROW_IN,
        GOAL_KICK,
        PENALTY_AWARDED,
        PENALTY_SAVED,
        PENALTY_MISSED,
        PENALTY_HIT_POST,
        INDIRECT_FREE_KICK,

        // Defensive Events
        SAVE,
        GOAL_LINE_CLEARANCE,
        BLOCK,
        INTERCEPTION,
        TACKLE,
        LAST_MAN_TACKLE,
        FOUL_COMMITTED,
        HAND_BALL,
        OFFSIDE,

        // Miscellaneous Events
        SHOT_ON_TARGET,
        SHOT_OFF_TARGET,
        SHOT_HIT_POST,
        CROSS,
        THROUGH_BALL,
        BIG_CHANCE_MISSED,
        BIG_CHANCE_CREATED,
        PASS_COMPLETED,
        PASS_INCOMPLETE,
        DRIBBLE_COMPLETED,
        DRIBBLE_FAILED,
        INJURY_STOPPAGE,
        VIDEO_REVIEW,
        FAN_INTERRUPTION,

        // Goalkeeper-Specific Events
        GK_PUNCH,
        GK_CATCH,
        GK_DROP,
        GK_SMOTHER,
        GK_DIVE_LEFT,
        GK_DIVE_RIGHT,
        GK_PENALTY_SAVE,
        GK_ERROR_LEADING_TO_GOAL,

        // Manager/Staff Events
        COACH_YELLOW,
        COACH_RED,
        BENCH_CLEARED,
        FOURTH_OFFICIAL_ADDED_TIME,

        // Weather/Field Events
        WEATHER_DELAY,
        FLOODLIGHT_FAILURE,
        PITCH_INVASION
    }

    // Helper methods for common scenarios
    public boolean involvesPlayer(Player player) {
        return player.equals(primaryPlayer) ||
                player.equals(secondaryPlayer) ||
                player.equals(tertiaryPlayer);
    }

    public boolean isGoalEvent() {
        return eventType == MatchEventType.GOAL ||
                eventType == MatchEventType.PENALTY_GOAL ||
                eventType == MatchEventType.FREE_KICK_GOAL ||
                eventType == MatchEventType.HEADED_GOAL ||
                eventType == MatchEventType.LONG_RANGE_GOAL ||
                eventType == MatchEventType.TAP_IN ||
                eventType == MatchEventType.OWN_GOAL;
    }
}