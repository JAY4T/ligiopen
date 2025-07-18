package com.jabulani.ligiopen.model.match.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "match_statistics")
public class MatchStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer shots;

    @Column(name = "shots_on_target")
    private Integer shotsOnTarget;

    private Double possession;

    @Column(name = "passing_accuracy")
    private Double passingAccuracy;

    private Integer corners;

    private Integer fouls;

    private Integer offsides;

    @Column(name = "yellow_card")
    private Integer yellowCards;

    @Column(name = "red_cards")
    private Integer redCards;

    private Boolean home;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "match_fixture_id")
    private MatchFixture matchFixture;
}
