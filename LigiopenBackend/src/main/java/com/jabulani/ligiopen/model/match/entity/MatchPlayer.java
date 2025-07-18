package com.jabulani.ligiopen.model.match.entity;

import com.jabulani.ligiopen.model.club.entity.Player;
import com.jabulani.ligiopen.model.match.MatchPlayerRole;
import com.jabulani.ligiopen.model.match.PlayerState;
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
@Table(name = "match_player")
public class MatchPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "match_fixture_id", nullable = false) // Links the player to the match fixture
    private MatchFixture matchFixture;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false) // Links the player entity
    private Player player;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false) // Role of the player in the match
    private MatchPlayerRole role;

    @Column(name = "club_name")
    private String clubName;

    @Column(name = "is_home_team", nullable = false) // Indicates if the player is on the home team
    private boolean isHomeTeam;
}
