package com.jabulani.ligiopen.entity.location;

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

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum SurfaceType {
        NATURAL_GRASS,
        ARTIFICIAL_TURF,
        DUSTY_GROUND,
        CONCRETE
    }
}