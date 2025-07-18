package com.jabulani.ligiopen.model.club.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "league")
public class League {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Club> clubs;

    @Enumerated(EnumType.STRING)
    @Column(name = "league_type")
    private LeagueType leagueType; // ENUM: GROUP_STAGE, KNOCKOUT, ROUND_ROBIN, HYBRID

    @Column(name = "current_stage")
    private String currentStage; // e.g., "Semi Final", "Quarter Final", "Group B"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    private Season season;

    enum LeagueType {
        GROUP_STAGE,
        KNOCKOUT,
        ROUND_ROBIN,
        HYBRID
    }

}
