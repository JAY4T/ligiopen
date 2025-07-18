package com.jabulani.ligiopen.model.club.entity;

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
@Table(name = "player_club")
public class PlayerClub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    private LocalDateTime createdAt;

    private LocalDateTime joinedOn;

    private LocalDateTime exitedOn;

    private Boolean archived;

    private Boolean isActive;

    private String playerRole;

    private String jerseyNumber;

    private LocalDateTime archivedAt;
}
