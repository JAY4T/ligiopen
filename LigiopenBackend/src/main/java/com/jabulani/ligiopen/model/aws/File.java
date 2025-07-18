package com.jabulani.ligiopen.model.aws;

import com.jabulani.ligiopen.model.club.entity.Club;
import com.jabulani.ligiopen.model.club.entity.Player;
import com.jabulani.ligiopen.model.match.entity.MatchCommentary;
import com.jabulani.ligiopen.model.match.entity.MatchLocation;
import com.jabulani.ligiopen.model.match.entity.PostMatchAnalysis;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "file")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id")
    private Player player;

    @OneToOne(mappedBy = "clubLogo", cascade = CascadeType.ALL)
    private Club clubAsLogo;

    @OneToOne(mappedBy = "clubMainPhoto", cascade = CascadeType.ALL)
    private Club clubAsMainPhoto;

    @OneToOne(mappedBy = "mainPhoto", cascade = CascadeType.ALL)
    private Player playerAsMainPhoto;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "match_location_id")
    private MatchLocation matchLocation;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "match_commentary_id")
    private MatchCommentary matchCommentary;
}
