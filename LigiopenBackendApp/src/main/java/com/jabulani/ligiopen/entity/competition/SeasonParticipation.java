package com.jabulani.ligiopen.entity.competition;

import com.jabulani.ligiopen.entity.club.Club;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "season_participations")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeasonParticipation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    private Season season;
    
    @Column(name = "registration_date")
    private LocalDate registrationDate;
    
    @Column(name = "registration_fee_paid")
    private Boolean registrationFeePaid = false;
    
    @Column(name = "payment_reference")
    private String paymentReference;

    @Column(name = "group_seed")
    private Integer groupSeed;
    
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "participation_status")
    @Enumerated(EnumType.STRING)
    private ParticipationStatus participationStatus = ParticipationStatus.REGISTERED;

    @Column(name = "withdrew_date")
    private LocalDate withdrewDate;

    @Column(name = "withdrawal_reason")
    private String withdrawalReason;
    
    public enum ParticipationStatus {
        REGISTERED, CONFIRMED, WITHDREW, DISQUALIFIED, EXPELLED
    }
}