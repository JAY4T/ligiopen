package com.jabulani.ligiopen.entity.competition;

import com.jabulani.ligiopen.entity.match.Match;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seasons")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Season {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id")
    private Competition competition;

    @Column(name = "name")
    private String name; // e.g., "2024/25 Season"

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "registration_start_date")
    private LocalDate registrationStartDate;

    @Column(name = "registration_end_date")
    private LocalDate registrationEndDate;

    @Column(name = "is_active")
    private Boolean isActive = false;

    @Column(name = "is_current")
    private Boolean isCurrent = false;

    @Column(name = "season_status")
    @Enumerated(EnumType.STRING)
    private SeasonStatus seasonStatus = SeasonStatus.UPCOMING;

    @Column(name = "max_participating_teams")
    private Integer maxParticipatingTeams;

    @Column(name = "current_participating_teams")
    private Integer currentParticipatingTeams = 0;

    @Column(name = "main_photo_id")
    private Long mainPhotoId;

    @Column(name = "photos_ids")
    private List<Long> photosIds;

    @Column(name = "rules", columnDefinition = "TEXT")
    private String rules;

    @Column(name = "prize_structure", columnDefinition = "JSON")
    private String prizeStructure;

    // Relationships
    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    private List<SeasonParticipation> participations = new ArrayList<>();

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    private List<Stage> stages = new ArrayList<>();

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    private List<Match> matches = new ArrayList<>();

    public enum SeasonStatus {
        UPCOMING, REGISTRATION_OPEN, REGISTRATION_CLOSED, 
        IN_PROGRESS, COMPLETED, CANCELLED, SUSPENDED
    }
}