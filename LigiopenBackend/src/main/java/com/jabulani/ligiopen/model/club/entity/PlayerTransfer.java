package com.jabulani.ligiopen.model.club.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "player_transfer")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_club_id")
    private Club fromClub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_club_id")
    private Club toClub;

    @Column(name = "transfer_date")
    private LocalDate transferDate;

    @Column(name = "transfer_type")
    private String transferType;

    private String reason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "transfer_verified")
    private Boolean transferVerified;
}
