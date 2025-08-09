package com.jabulani.ligiopen.entity.user;

import com.jabulani.ligiopen.entity.club.Club;
import com.jabulani.ligiopen.entity.club.FavoritedClub;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, unique = true)
    private String username;

    @Column(nullable = true, unique = true)
    private String email;

    @Column(nullable = true)
    private String password; // Store hashed password only

    @Column(name = "google_id")
    private String googleId; // Google's unique identifier

    @Column(name = "google_email")
    private String googleEmail; // Email associated with Google account

    @Column(name = "email_verified")
    @Builder.Default
    private boolean emailVerified = false;

    @Column(name = "account_enabled")
    @Builder.Default
    private boolean accountEnabled = true;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "profile_picture_id")
    private Long profilePictureId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "preferred_language")
    private String preferredLanguage = "en";

    // Timestamps
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<FavoritedClub> favoritedClubs = new ArrayList<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Club> ownedClubs = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "user_club_management",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "club_id")
    )
    private List<Club> managedClubs = new ArrayList<>();

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum UserRole {
        USER,
        CLUB_ADMIN,
        CONTENT_REVIEW_ADMIN,
        SUPER_ADMIN
    }
}