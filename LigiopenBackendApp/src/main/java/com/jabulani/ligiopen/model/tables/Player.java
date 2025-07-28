package com.jabulani.ligiopen.model.tables;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private LocalDate dob;

    private String nationality;

    @Column(name = "height_cm")
    private Double heightCm;

    @Column(name = "weight_kg")
    private Double weightKg;

    @Column(name = "preferred_foot")
    @Enumerated(EnumType.STRING)
    private PreferredFoot preferredFoot;

    @Enumerated(EnumType.STRING)
    private Position position;

    @Column(name = "main_photo_id")
    private Long mainPhotoId;

    @Column(name = "photos_ids")
    private List<Long> photosIds;

    enum Position {
        GOALKEEPER,
        DEFENDER,
        MIDFIELDER,
        FORWARD
    }

    enum PreferredFoot {
        LEFT,
        RIGHT,
        BOTH
    }
}
