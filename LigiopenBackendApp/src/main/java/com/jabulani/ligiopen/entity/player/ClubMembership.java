package com.jabulani.ligiopen.entity.player;

import com.jabulani.ligiopen.entity.club.Club;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "club_memberships")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubMembership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;
    
    @Column(name = "jersey_number")
    private Integer jerseyNumber;
    
    @Column(name = "position")
    @Enumerated(EnumType.STRING)
    private Player.Position position;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "joined_date")
    private LocalDate joinedDate;
    
    @Column(name = "left_date")
    private LocalDate leftDate;
    
    @Column(name = "is_captain")
    private Boolean isCaptain = false;

    @Column(name = "is_vice_captain")
    private Boolean isViceCaptain = false;
    
    @Column(name = "contract_type")
    @Enumerated(EnumType.STRING)
    private ContractType contractType;

    @Column(name = "contract_start_date")
    private LocalDate contractStartDate;

    @Column(name = "contract_end_date")
    private LocalDate contractEndDate;

    @Column(name = "weekly_wage")
    private BigDecimal weeklyWage;

    @Column(name = "signing_fee")
    private BigDecimal signingFee;

    @Column(name = "membership_status")
    @Enumerated(EnumType.STRING)
    private MembershipStatus membershipStatus = MembershipStatus.ACTIVE;
    
    public enum ContractType {
        PROFESSIONAL, AMATEUR, YOUTH, TRIAL, LOAN
    }

    public enum MembershipStatus {
        ACTIVE, INJURED, SUSPENDED, ON_LOAN, TERMINATED
    }
}