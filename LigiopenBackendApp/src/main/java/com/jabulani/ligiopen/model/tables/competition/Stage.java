package com.jabulani.ligiopen.model.tables.competition;

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
@Table(name = "`stages`")
public class Stage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "season_id")
    private Season season;

    private String name;

    @Column(name = "`order`")
    private int order;

    @Column(name = "stage_type")
    @Enumerated(EnumType.STRING)
    private StageType type;

    enum StageType {
        GROUP,
        KNOCKOUT,
        LEAGUE
    }
}
