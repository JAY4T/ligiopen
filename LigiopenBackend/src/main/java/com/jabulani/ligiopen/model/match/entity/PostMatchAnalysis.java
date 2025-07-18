package com.jabulani.ligiopen.model.match.entity;

import com.jabulani.ligiopen.model.aws.File;
import com.jabulani.ligiopen.model.club.entity.Player;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "post_match_analysis")
public class PostMatchAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "match_fixture_id", nullable = false)
    private MatchFixture matchFixture;

    @Column(name = "home_club_score")
    private Integer homeClubScore;

    @Column(name = "away_club_score")
    private Integer awayClubScore;

    @OneToOne
    @JoinColumn(name = "man_of_the_match_id")
    private Player manOfTheMatch;

    @OneToMany(mappedBy = "postMatchAnalysis", cascade = CascadeType.ALL)
    private List<MatchCommentary> minuteByMinuteCommentary;

    @Enumerated(EnumType.STRING)
    private PostMatchAnalysisStatus postMatchAnalysisStatus;
}
