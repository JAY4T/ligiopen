package com.jabulani.ligiopen.entity.competition;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "promotion_relegation_rules")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionRelegationRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_competition_id")
    private Competition fromCompetition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_competition_id")
    private Competition toCompetition;

    @Column(name = "rule_type")
    @Enumerated(EnumType.STRING)
    private RuleType ruleType;

    @Column(name = "start_position")
    private Integer startPosition; // Starting position range

    @Column(name = "end_position")
    private Integer endPosition; // Ending position range

    @Column(name = "num_teams")
    private Integer numTeams;

    @Column(name = "requires_playoff")
    private Boolean requiresPlayoff = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    public enum RuleType {
        PROMOTION, RELEGATION, PLAYOFF_PROMOTION, PLAYOFF_RELEGATION
    }
}