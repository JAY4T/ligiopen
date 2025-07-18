package com.jabulani.ligiopen.model.match.entity.events;

import com.jabulani.ligiopen.model.club.entity.Player;
import com.jabulani.ligiopen.model.match.MatchEventType;
import com.jabulani.ligiopen.model.match.entity.MatchCommentary;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Use JOINED or SINGLE_TABLE strategy
@Table(name = "match_event")
public class MatchEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String summary;

    @Column(name = "minute", nullable = false)
    private String minute;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @Enumerated(EnumType.STRING)
    private MatchEventType matchEventType;

    @OneToOne(mappedBy = "matchEvent", cascade = CascadeType.ALL)
    private MatchCommentary matchCommentary;
}
