package com.jabulani.ligiopen.entity.competition;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "competitions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Competition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "competition_type")
    @Enumerated(EnumType.STRING)
    private CompetitionType type;

    @Column(name = "competition_format")
    @Enumerated(EnumType.STRING)
    private CompetitionFormat format;

    @Column(name = "competition_level")
    @Enumerated(EnumType.STRING)
    private CompetitionLevel level;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "age_category")
    @Enumerated(EnumType.STRING)
    private AgeCategory ageCategory;

    @Column(name = "max_teams")
    private Integer maxTeams;

    @Column(name = "registration_fee")
    private BigDecimal registrationFee;

    @Column(name = "prize_money")
    private BigDecimal prizeMoney;

    @Column(name = "organizer")
    private String organizer;

    @Column(name = "sponsor")
    private String sponsor;

    @Column(name = "main_photo_id")
    private Long mainPhotoId;

    @Column(name = "photos_ids")
    private List<Long> photosIds;

    @Column(name = "logo_file_id")
    private Long logoFileId;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Relationships
    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL)
    private List<Season> seasons = new ArrayList<>();

    public enum CompetitionType {
        LEAGUE, CUP, TOURNAMENT, FRIENDLY
    }

    public enum CompetitionFormat {
        ROUND_ROBIN, GROUP_STAGE_KNOCKOUT, STRAIGHT_KNOCKOUT, MIXED
    }

    public enum CompetitionLevel {
        PREMIER_LEAGUE, DIVISION_1, DIVISION_2, DIVISION_3, 
        GRASSROOTS, COUNTY, NATIONAL, INTERNATIONAL
    }

    public enum Gender {
        MALE, FEMALE, MIXED
    }

    public enum AgeCategory {
        SENIOR, U23, U21, U19, U17, U15, U13, OPEN
    }
}