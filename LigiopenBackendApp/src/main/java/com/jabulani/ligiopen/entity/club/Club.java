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

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "verification_status")
    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

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

    public enum VerificationStatus {
        PENDING, VERIFIED, REJECTED, SUSPENDED
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