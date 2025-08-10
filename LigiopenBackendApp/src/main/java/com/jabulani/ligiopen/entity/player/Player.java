package com.jabulani.ligiopen.entity.player;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "players")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "place_of_birth")
    private String placeOfBirth;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "second_nationality")
    private String secondNationality;

    @Column(name = "height_cm")
    private Double heightCm;

    @Column(name = "weight_kg")
    private Double weightKg;

    @Column(name = "preferred_foot")
    @Enumerated(EnumType.STRING)
    private PreferredFoot preferredFoot;

    @Column(name = "primary_position")
    @Enumerated(EnumType.STRING)
    private Position primaryPosition;

    @Column(name = "secondary_positions")
    @Enumerated(EnumType.STRING)
    private List<Position> secondaryPositions;

    @Column(name = "main_photo_id")
    private Long mainPhotoId;

    @Column(name = "photos_ids")
    private List<Long> photosIds;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "emergency_contact_name")
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone")
    private String emergencyContactPhone;

    @Column(name = "emergency_contact_relationship")
    private String emergencyContactRelationship;

    @Column(name = "id_number")
    private String idNumber; // Kenyan National ID

    @Column(name = "passport_number")
    private String passportNumber;

    @Column(name = "fkf_registration_number")
    private String fkfRegistrationNumber; // FKF player registration

    @Column(name = "market_value")
    private BigDecimal marketValue;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<ClubMembership> clubMemberships = new ArrayList<>();

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<PlayerTransfer> transfers = new ArrayList<>();

    public enum Position {
        // Goalkeeper
        GOALKEEPER,
        
        // Defenders
        RIGHT_BACK, LEFT_BACK, CENTRE_BACK, SWEEPER, WING_BACK,
        
        // Midfielders
        DEFENSIVE_MIDFIELDER, CENTRAL_MIDFIELDER, ATTACKING_MIDFIELDER,
        RIGHT_MIDFIELDER, LEFT_MIDFIELDER, WINGER,
        
        // Forwards
        STRIKER, CENTRE_FORWARD, RIGHT_WINGER, LEFT_WINGER, 
        SECOND_STRIKER, FALSE_NINE
    }

    public enum PreferredFoot {
        LEFT, RIGHT, BOTH
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