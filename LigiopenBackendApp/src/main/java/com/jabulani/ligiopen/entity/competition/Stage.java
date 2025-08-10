package com.jabulani.ligiopen.entity.competition;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "`stages`")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Stage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "season_id")
    private Season season;

    @Column(name = "name")
    private String name;

    @Column(name = "stage_order")
    private Integer stageOrder;

    @Column(name = "stage_type")
    @Enumerated(EnumType.STRING)
    private StageType type;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    @Column(name = "advancement_rules", columnDefinition = "TEXT")
    private String advancementRules;

    // Relationships
    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL)
    private List<Group> groups = new ArrayList<>();

    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL)
    private List<KnockoutBracket> knockoutBrackets = new ArrayList<>();

    public enum StageType {
        GROUP, KNOCKOUT, LEAGUE, PLAYOFF
    }
}