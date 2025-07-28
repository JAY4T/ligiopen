package com.jabulani.ligiopen.model.tables;

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
@Table(name = "promotion_relegation_rules")
public class PromotionRelegationRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id")
    private Competition competition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_match_id")
    private Match sourceMatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_match_id")
    private Match targetMatch;

    @Column(name = "num_teams_promoted")
    private int numTeamsPromoted;

    @Column(name = "num_teams_relegated")
    private int numTeamsRelegated;

    @Column(name = "playoff_included")
    private Boolean playoffIncluded;
}
