package com.jabulani.ligiopen.entity.competition;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "knockout_brackets")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KnockoutBracket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id")
    private Stage stage;

    @Column(name = "name")
    private String name;

    @Column(name = "round_number")
    private Integer roundNumber;

    @Column(name = "round_name")
    private String roundName; // e.g., "Quarter-Finals", "Semi-Finals"

    @Column(name = "is_two_legged")
    private Boolean isTwoLegged = false;

    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    // Relationships
    @OneToMany(mappedBy = "bracket", cascade = CascadeType.ALL)
    private List<KnockoutProgression> progressions = new ArrayList<>();
}