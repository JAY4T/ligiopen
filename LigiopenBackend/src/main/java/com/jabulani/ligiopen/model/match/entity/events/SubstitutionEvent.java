package com.jabulani.ligiopen.model.match.entity.events;

import com.jabulani.ligiopen.model.club.entity.Player;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
//@Builder
@EqualsAndHashCode(callSuper = true)
@Table(name = "substitution_event")
public class SubstitutionEvent extends MatchEvent {
    @ManyToOne
    @JoinColumn(name = "subbed_out_player_id", nullable = false)
    private Player subbedOutPlayer;

}
