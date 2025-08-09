package com.jabulani.ligiopen.model.tables.team_and_player;

import com.jabulani.ligiopen.model.tables.FavoritedClub;
import com.jabulani.ligiopen.model.tables.Stadium;
import com.jabulani.ligiopen.model.tables.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "clubs")
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String abbreviation;

    private LocalDate founded;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id")
    private Stadium stadium;

    private String country;

    @Column(name = "main_photo_id")
    private Long mainPhotoId;

    @Column(name = "photos_ids")
    private List<Long> photosIds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<FavoritedClub> favoritedByUsers = new ArrayList<>();
}
