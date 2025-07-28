package com.jabulani.ligiopen.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "club_staff_members")
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

    @Column(name = "staff_role")
    @Enumerated(EnumType.STRING)
    private StaffRole role;

    @Column(name = "other_role")
    private String otherRole;

    @Column(name = "joined_date")
    private LocalDate joinedDate;

    @Column(name = "main_photo_id")
    private Long mainPhotoId;

    @Column(name = "photos_ids")
    private List<Long> photosIds;

    enum StaffRole {
        MANAGER,
        COACH,
        ASSISTANT_COACH,
        CONTENT_MANAGER,
        ADMIN,
        EDITOR,
        TREASURER,
        SECRETARY,
        PHYSIO,
        DOCTOR,
        CLEANER,
        OTHER
    }
}
