package com.jabulani.ligiopen.model.match.entity;

import com.jabulani.ligiopen.model.aws.File;
import com.jabulani.ligiopen.model.match.entity.events.MatchEvent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "match_commentary")
public class MatchCommentary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_match_analysis_id")
    private PostMatchAnalysis postMatchAnalysis;

    @OneToMany(mappedBy = "matchCommentary", cascade = CascadeType.ALL)
    private List<File> files;

    private String minute;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private Boolean archived;

//    @Enumerated(EnumType.STRING)
//    private MatchCommentaryStatus matchCommentaryStatus;

    private LocalDateTime archivedAt;

    // Add relationship to MatchEvent
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "match_event_id")
    private MatchEvent matchEvent;
}
