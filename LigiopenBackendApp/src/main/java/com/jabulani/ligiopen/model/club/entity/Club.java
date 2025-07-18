package com.jabulani.ligiopen.model.club.entity;

import com.jabulani.ligiopen.model.aws.File;
import com.jabulani.ligiopen.model.match.entity.MatchFixture;
import com.jabulani.ligiopen.model.match.entity.MatchLocation;
import com.jabulani.ligiopen.model.news.News;
import com.jabulani.ligiopen.model.user.entity.UserAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "club")
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;

    @Column(unique = false, name = "club_abbreviation")
    private String clubAbbreviation;

    @ManyToOne
    @JoinColumn(name = "home_location_id")
    private MatchLocation home;

    private String description;
    private String country;
    private String county;
    private String town;
    private LocalDate startedOn;
    private LocalDateTime createdAt;
    private Boolean archived;
    private LocalDateTime archivedAt;

    // Team Admin relationship (one-to-one as a club has one admin)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_admin_id")
    private UserAccount teamAdmin;

    // Managers relationship (one-to-many as a club can have multiple managers)
    @OneToMany(mappedBy = "managedClub", fetch = FetchType.LAZY)
    private List<UserAccount> managers;

    // Coaches relationship (one-to-many as a club can have multiple coaches)
    @OneToMany(mappedBy = "coachedClub", fetch = FetchType.LAZY)
    private List<UserAccount> coaches;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PlayerClub> playerClubs;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<File> files;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "club_logo_id")
    private File clubLogo;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "club_main_photo_id")
    private File clubMainPhoto;

    @OneToMany(mappedBy = "homeClub", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MatchFixture> homeFixtures;

    @OneToMany(mappedBy = "awayClub", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MatchFixture> awayFixtures;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "club_news",
            joinColumns = @JoinColumn(name = "club_id"),
            inverseJoinColumns = @JoinColumn(name = "news_id")
    )
    private List<News> news;

    @ManyToMany(mappedBy = "favoriteClubs")
    private List<UserAccount> bookmarkedBy = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id", nullable = false)
    private League league;

    @Enumerated(EnumType.STRING)
    private ClubStatus clubStatus;
}