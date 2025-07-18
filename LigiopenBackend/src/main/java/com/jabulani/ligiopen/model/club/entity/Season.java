package com.jabulani.ligiopen.model.club.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "season")
public class Season {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name; // e.g., "2024/2025"

    private LocalDate startDate;
    private LocalDate endDate;

    private Boolean isActive;

    @OneToMany(mappedBy = "season")
    private List<League> leagues;
}
