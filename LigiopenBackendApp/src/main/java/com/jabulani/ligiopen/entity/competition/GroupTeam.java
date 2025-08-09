package com.jabulani.ligiopen.entity.competition;

import com.jabulani.ligiopen.entity.club.Club;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "group_teams")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @Column(name = "seed")
    private Integer seed;

    @Column(name = "final_position")
    private Integer finalPosition;

    @Column(name = "advanced_to_next_stage")
    private Boolean advancedToNextStage = false;
}