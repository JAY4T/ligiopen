package com.jabulani.ligiopen.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "competitions")
public class Competition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "competition_type")
    @Enumerated(EnumType.STRING)
    private CompetitionType type;

    @Column(name = "competition_format")
    @Enumerated(EnumType.STRING)
    private CompetitionFormat format;

    @Column(name = "main_photo_id")
    private Long mainPhotoId;

    @Column(name = "photos_ids")
    private List<Long> photosIds;

    enum CompetitionType {
        LEAGUE,
        TOURNAMENT,
        CUP
    }

    enum CompetitionFormat {
        ROUND_ROBIN,
        GROUP_STAGE,
        KNOCKOUT,
        MIXED
    }
}
