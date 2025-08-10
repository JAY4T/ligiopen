package com.jabulani.ligiopen.entity.competition;

import com.jabulani.ligiopen.entity.match.Match;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "knockout_progressions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KnockoutProgression {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "knockout_bracket_id")
    private KnockoutBracket bracket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_match_id")
    private Match sourceMatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_match_id")
    private Match targetMatch;

    @Column(name = "progression_rule")
    @Enumerated(EnumType.STRING)
    private ProgressionRule progressionRule;

    public enum ProgressionRule {
        WINNER, LOSER, HOME_TEAM, AWAY_TEAM
    }
}