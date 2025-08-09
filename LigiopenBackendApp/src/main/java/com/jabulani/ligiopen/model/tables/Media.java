package com.jabulani.ligiopen.model.tables;

import com.jabulani.ligiopen.model.tables.match_tracking.Match;
import com.jabulani.ligiopen.model.tables.match_tracking.MatchEvent;
import com.jabulani.ligiopen.model.tables.team_and_player.Player;
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
@Table(name = "media")
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne
    @JoinColumn(name = "match_event_id")
    private MatchEvent matchEvent;

    @ManyToOne
    @JoinColumn(name = "match_commentary_id")
    private MatchCommentary matchCommentary;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(name = "media_source")
    @Enumerated(EnumType.STRING)
    private MediaSource mediaSource;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;

    private String url;

    @Column(name = "duration_sec")
    private int durationSec;

    @Column(name = "media_type")
    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    enum MediaSource {
        LOCAL,
        YOUTUBE,
        EXTERNAL_URL
    }

    enum MediaType {
        PHOTO,
        VIDEO
    }
}
