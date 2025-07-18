package com.jabulani.ligiopen.model.club.entity;

import com.jabulani.ligiopen.model.match.entity.MatchFixture;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "league_stage")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeagueStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name; // e.g., "Quarter Final", "Group A"

    @ManyToOne
    @JoinColumn(name = "league_id")
    private League league;

    private Integer sequence; // Order of the stage (1 = group, 2 = QF, etc.)

    @OneToMany(mappedBy = "leagueStage", cascade = CascadeType.ALL)
    private List<MatchFixture> matchFixtures;
}
