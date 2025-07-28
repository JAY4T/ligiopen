package com.jabulani.ligiopen.model;

import com.jabulani.ligiopen.model.aws.File;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "stadiums")
public class Stadium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String city;

    private String town;

    private String county;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String country;

    private Integer capacity;

    private Boolean shared;

    @Column(name = "surface_type")
    @Enumerated(EnumType.STRING)
    private SurfaceType surfaceType;

    @Column(name = "main_photo_id")
    private Long mainPhotoId;

    @Column(name = "photos_ids")
    private List<Long> photosIds;

    enum SurfaceType {
        GRASS,
        ARTIFICIAL_TURF,
        DUSTY_GROUND,
    }

}
