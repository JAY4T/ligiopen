package com.jabulani.ligiopen.model.match.entity.events;

import com.jabulani.ligiopen.model.club.entity.Player;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
//@Builder
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "goal_event")
public class GoalEvent extends MatchEvent {
    @ManyToOne
    @JoinColumn(name = "assisting_player_id")
    private Player assistingPlayer;
}
