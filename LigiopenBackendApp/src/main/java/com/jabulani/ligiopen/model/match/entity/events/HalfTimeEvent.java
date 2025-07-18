package com.jabulani.ligiopen.model.match.entity.events;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor(force = true)
//@AllArgsConstructor
//@Builder
@EqualsAndHashCode(callSuper = true)
@Table(name = "half_time_event")
public class HalfTimeEvent extends MatchEvent {
    private Integer homeClubScore;
    private Integer awayClubScore;
}
