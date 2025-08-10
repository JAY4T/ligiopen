package com.jabulani.ligiopen.entity.file;

import com.jabulani.ligiopen.entity.club.Club;
import com.jabulani.ligiopen.entity.match.Match;
import com.jabulani.ligiopen.entity.match.MatchCommentary;
import com.jabulani.ligiopen.entity.match.MatchEvent;
import com.jabulani.ligiopen.entity.player.Player;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "media")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    @Column(name = "media_source")
    @Enumerated(EnumType.STRING)
    private MediaSource mediaSource;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;

    @Column(name = "external_url")
    private String externalUrl;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "duration_sec")
    private Integer durationSec;

    @Column(name = "media_type")
    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "view_count")
    private Long viewCount = 0L;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum MediaSource {
        LOCAL, YOUTUBE, EXTERNAL_URL, INSTAGRAM, TIKTOK
    }

    public enum MediaType {
        PHOTO, VIDEO, AUDIO, LIVESTREAM
    }
}