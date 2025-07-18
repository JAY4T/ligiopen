package com.jabulani.ligiopen.model.club.entity;

import com.jabulani.ligiopen.model.aws.File;
import com.jabulani.ligiopen.model.club.PlayerPosition;
import com.jabulani.ligiopen.model.match.PlayerState;
import com.jabulani.ligiopen.model.user.entity.UserAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "main_photo_id")
    private File mainPhoto;

    private String username;

    private Integer number;

    @Enumerated(EnumType.STRING)
    private PlayerPosition playerPosition;

    private Integer age;

    private Double height;

    private Double weight;

    private String country;

    private String county;

    private String town;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<PlayerClub> playerClubs;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id")
//    private UserAccount userAccount;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<File> files;

    @Enumerated(EnumType.STRING)
    @Column(name = "player_state", nullable = false)
    private PlayerState playerState;

}
