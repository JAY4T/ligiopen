package com.jabulani.ligiopen.entity.match;

import com.jabulani.ligiopen.entity.club.Club;
import com.jabulani.ligiopen.entity.player.Player;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "match_lineups")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchLineup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(name = "jersey_number")
    private Integer jerseyNumber;

    @Column(name = "position")
    @Enumerated(EnumType.STRING)
    private Player.Position position;

    @Column(name = "is_starter")
    private Boolean isStarter = true;

    @Column(name = "is_captain")
    private Boolean isCaptain = false;

    @Column(name = "substituted_in_minute")
    private Integer substitutedInMinute;

    @Column(name = "substituted_out_minute")
    private Integer substitutedOutMinute;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "substituted_by_player_id")
    private Player substitutedByPlayer;

    @Column(name = "formation_position_x")
    private Double formationPositionX;

    @Column(name = "formation_position_y")
    private Double formationPositionY;
}