package com.jabulani.ligiopen.entity.match;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "match_commentaries")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchCommentary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_event_id")
    private MatchEvent matchEvent;

    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "author_name")
    private String authorName;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "minute")
    private Integer minute;

    @Column(name = "added_time")
    private Integer addedTime;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "is_key_event")
    private Boolean isKeyEvent = false;

    @Column(name = "commentary_type")
    @Enumerated(EnumType.STRING)
    private CommentaryType commentaryType = CommentaryType.LIVE;

    public enum CommentaryType {
        PRE_MATCH, LIVE, HALF_TIME, POST_MATCH, ANALYSIS
    }
}