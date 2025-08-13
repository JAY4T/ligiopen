package com.jabulani.ligiopen.entity.location;

import com.jabulani.ligiopen.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "stadiums")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Stadium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "city")
    private String city;

    @Column(name = "town")
    private String town;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "county_id")
    private County county;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "is_shared")
    private Boolean isShared = false;

    @Column(name = "surface_type")
    @Enumerated(EnumType.STRING)
    private SurfaceType surfaceType;

    @Column(name = "has_floodlights")
    private Boolean hasFloodlights = false;

    @Column(name = "has_roof")
    private Boolean hasRoof = false;

    @Column(name = "year_built")
    private Integer yearBuilt;

    @Column(name = "main_photo_id")
    private Long mainPhotoId;

    @Column(name = "photos_ids")
    private List<Long> photosIds;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "rental_fee")
    private BigDecimal rentalFee;

    // Stadium approval workflow fields
    @Column(name = "approval_status")
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus = ApprovalStatus.APPROVED; // Default for existing stadiums

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by_user_id")
    private UserEntity submittedBy;

    @Column(name = "submission_date")
    private LocalDateTime submissionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_user_id")
    private UserEntity approvedBy;

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

    @Column(name = "approval_notes", columnDefinition = "TEXT")
    private String approvalNotes;

    // Enhanced stadium features
    @Column(name = "has_parking")
    private Boolean hasParking = false;

    @Column(name = "has_concessions")
    private Boolean hasConcessions = false;

    @Column(name = "has_wifi_internet")
    private Boolean hasWifiInternet = false;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "management_company")
    private String managementCompany;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum SurfaceType {
        NATURAL_GRASS,
        ARTIFICIAL_TURF,
        DUSTY_GROUND,
        CONCRETE,
        MIXED_SURFACE
    }

    public enum ApprovalStatus {
        PENDING,      // Newly submitted, awaiting review
        APPROVED,     // Approved and visible to users
        REJECTED,     // Rejected with reason
        MERGED,       // Merged with existing stadium (duplicate)
        UNDER_REVIEW  // Being reviewed by admin
    }
}