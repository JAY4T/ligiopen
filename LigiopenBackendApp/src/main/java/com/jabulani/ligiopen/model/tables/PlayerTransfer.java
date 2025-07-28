package com.jabulani.ligiopen.model.tables;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "player_transfers")
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

    @Column(name = "contract_until")
    private LocalDate contractUntil;

    @Column(name = "transfer_type")
    @Enumerated(EnumType.STRING)
    private TransferType transferType;

    enum TransferType {
        PERMANENT,
        LOAN,
        FREE
    }
}
