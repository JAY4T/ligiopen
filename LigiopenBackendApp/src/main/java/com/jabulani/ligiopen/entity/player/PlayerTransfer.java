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
@Table(name = "player_transfers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "from_club_id")
    private Club fromClub;

    @ManyToOne
    @JoinColumn(name = "to_club_id")
    private Club toClub;

    @Column(name = "transfer_date")
    private LocalDate transferDate;

    @Column(name = "transfer_fee")
    private BigDecimal transferFee;

    @Column(name = "agent_fee")
    private BigDecimal agentFee;

    @Column(name = "contract_until")
    private LocalDate contractUntil;

    @Column(name = "transfer_type")
    @Enumerated(EnumType.STRING)
    private TransferType transferType;

    @Column(name = "transfer_status")
    @Enumerated(EnumType.STRING)
    private TransferStatus transferStatus = TransferStatus.PENDING;

    @Column(name = "transfer_window")
    private String transferWindow; // e.g., "Summer 2024", "Winter 2024"

    @Column(name = "announcement_date")
    private LocalDate announcementDate;

    @Column(name = "medical_completed")
    private Boolean medicalCompleted = false;

    @Column(name = "contract_signed")
    private Boolean contractSigned = false;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    public enum TransferType {
        PERMANENT, LOAN, FREE_TRANSFER, MUTUAL_TERMINATION
    }

    public enum TransferStatus {
        PENDING, COMPLETED, CANCELLED, FAILED
    }
}