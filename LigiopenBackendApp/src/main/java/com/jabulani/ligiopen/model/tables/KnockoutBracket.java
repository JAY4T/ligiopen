package com.jabulani.ligiopen.model.tables;

import com.jabulani.ligiopen.model.tables.competition.Stage;
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
@Table(name = "knockout_brackets")
public class KnockoutBracket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id")
    private Stage stage;

    private String name;

    private int round;

    @Column(name = "is_two_legged")
    private Boolean isTwoLegged;
}
