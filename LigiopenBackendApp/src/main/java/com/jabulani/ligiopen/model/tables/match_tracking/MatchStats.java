package com.jabulani.ligiopen.model.tables.match_tracking;

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
@Table(name = "match_stats")
public class MatchStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer shots;

    private Double possession;

    @Column(name = "pass_accuracy")
    private Double passAccuracy;
}
