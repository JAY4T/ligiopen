package com.jabulani.ligiopen.entity.player;

import com.jabulani.ligiopen.entity.club.Club;
import com.jabulani.ligiopen.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "club_invitations")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubInvitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_by_user_id")
    private UserEntity invitedBy;
    
    @Column(name = "invitation_status")
    @Enumerated(EnumType.STRING)
    private InvitationStatus status = InvitationStatus.PENDING;
    
    @Column(name = "invited_at")
    private LocalDateTime invitedAt;
    
    @Column(name = "responded_at")
    private LocalDateTime respondedAt;
    
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    
    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "proposed_jersey_number")
    private Integer proposedJerseyNumber;

    @Column(name = "proposed_position")
    @Enumerated(EnumType.STRING)
    private Player.Position proposedPosition;

    @Column(name = "proposed_wage")
    private BigDecimal proposedWage;
    
    public enum InvitationStatus {
        PENDING, ACCEPTED, REJECTED, EXPIRED, CANCELLED
    }
}