package com.jabulani.ligiopen.entity.competition;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "`groups`")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stage_id")
    private Stage stage;

    @Column(name = "name")
    private String name; // e.g., "Group A"

    @Column(name = "advancing_teams")
    private Integer advancingTeams;

    @Column(name = "max_teams")
    private Integer maxTeams;

    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    // Relationships
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<GroupTeam> groupTeams = new ArrayList<>();
}