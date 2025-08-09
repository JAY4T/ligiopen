package com.jabulani.ligiopen.entity.club;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "club_staff_members")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubStaffMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "staff_role")
    @Enumerated(EnumType.STRING)
    private StaffRole role;

    @Column(name = "other_role")
    private String otherRole;

    @Column(name = "joined_date")
    private LocalDate joinedDate;

    @Column(name = "left_date")
    private LocalDate leftDate;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "salary")
    private BigDecimal salary;

    @Column(name = "biography", columnDefinition = "TEXT")
    private String biography;

    @Column(name = "main_photo_id")
    private Long mainPhotoId;

    @Column(name = "photos_ids")
    private List<Long> photosIds;

    @Column(name = "license_number")
    private String licenseNumber; // For coaches

    @Column(name = "experience_years")
    private Integer experienceYears;

    public enum StaffRole {
        HEAD_COACH,
        ASSISTANT_COACH,
        TECHNICAL_DIRECTOR,
        MANAGER,
        CAPTAIN, // Player captain
        VICE_CAPTAIN,
        CONTENT_MANAGER,
        SOCIAL_MEDIA_ADMIN,
        TREASURER,
        SECRETARY,
        PHYSIOTHERAPIST,
        DOCTOR,
        KIT_MANAGER,
        GROUNDSKEEPER,
        SECURITY,
        OTHER
    }
}