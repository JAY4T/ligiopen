package com.jabulani.ligiopen.entity.club;

import com.jabulani.ligiopen.entity.location.County;
import com.jabulani.ligiopen.entity.location.Stadium;
import com.jabulani.ligiopen.entity.player.ClubMembership;
import com.jabulani.ligiopen.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clubs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "abbreviation")
    private String abbreviation;

    @Column(name = "founded")
    private LocalDate founded;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_stadium_id")
    private Stadium homeStadium;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "county_id")
    private County county;

    @Column(name = "colors")
    private String colors;

    @Column(name = "registration_number")
    private String registrationNumber; // FKF registration number

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "social_media_links", columnDefinition = "JSON")
    private String socialMediaLinks;

    @Column(name = "club_level")
    @Enumerated(EnumType.STRING)
    private ClubLevel clubLevel;

    @Column(name = "main_photo_id")
    private Long mainPhotoId;

    @Column(name = "photos_ids")
    private List<Long> photosIds;

    @Column(name = "logo_file_id")
    private Long logoFileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id")
    private UserEntity owner;

    @ManyToMany(mappedBy = "managedClubs")
    private List<UserEntity> managers = new ArrayList<>();

    // LigiOpen internal verification
    @Column(name = "is_ligiopen_verified")
    private Boolean isLigiopenVerified = false;

    @Column(name = "ligiopen_verification_status")
    @Enumerated(EnumType.STRING)
    private LigiopenVerificationStatus ligiopenVerificationStatus = LigiopenVerificationStatus.PENDING;

    @Column(name = "ligiopen_verification_date")
    private LocalDateTime ligiopenVerificationDate;

    @Column(name = "ligiopen_verification_notes", columnDefinition = "TEXT")
    private String ligiopenVerificationNotes;

    // FKF official verification
    @Column(name = "is_fkf_verified")
    private Boolean isFkfVerified = false;

    @Column(name = "fkf_verification_status")
    @Enumerated(EnumType.STRING)
    private FkfVerificationStatus fkfVerificationStatus = FkfVerificationStatus.NOT_APPLICABLE;

    @Column(name = "fkf_verification_date")
    private LocalDateTime fkfVerificationDate;

    @Column(name = "fkf_registration_date")
    private LocalDate fkfRegistrationDate;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<FavoritedClub> favoritedByUsers = new ArrayList<>();

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<ClubMembership> clubMemberships = new ArrayList<>();

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<ClubStaffMember> staffMembers = new ArrayList<>();

    public enum ClubLevel {
        GRASSROOTS,
        DIVISION_3,
        DIVISION_2, 
        DIVISION_1,
        PREMIER_LEAGUE,
        NATIONAL_TEAM,
        WOMEN_PREMIER,
        YOUTH_LEAGUE
    }

    public enum LigiopenVerificationStatus {
        PENDING,        // Awaiting LigiOpen crew review
        VERIFIED,       // Verified by LigiOpen as legitimate club
        REJECTED,       // Rejected by LigiOpen (fake/duplicate club)
        SUSPENDED,      // Temporarily suspended by LigiOpen
        UNDER_REVIEW    // Currently being reviewed by crew
    }

    public enum FkfVerificationStatus {
        NOT_APPLICABLE, // Grassroots club, no FKF registration needed
        PENDING,        // Has FKF registration number but not verified
        VERIFIED,       // FKF registration confirmed as valid
        EXPIRED,        // FKF registration has expired
        INVALID,        // FKF registration number is invalid
        SUSPENDED       // Suspended by FKF
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}