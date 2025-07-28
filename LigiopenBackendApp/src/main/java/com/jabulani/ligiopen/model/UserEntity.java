package com.jabulani.ligiopen.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
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

    // Timestamps
    @Column(updatable = false, name = " created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<FavoritedClub> favoritedClubs = new ArrayList<>();

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public UserEntity(UserEntity user) {
    }

    public enum UserRole {
        USER,
        CONTENT_REVIEW_ADMIN,
        SUPER_ADMIN
    }

}