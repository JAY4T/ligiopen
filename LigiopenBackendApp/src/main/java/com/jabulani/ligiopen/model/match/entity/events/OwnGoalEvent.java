package com.jabulani.ligiopen.model.match.entity.events;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Data
//@AllArgsConstructor
@NoArgsConstructor(force = true)
//@Builder
@EqualsAndHashCode(callSuper = true)
@Table(name = "own_goal_event")
public class OwnGoalEvent extends GoalEvent {
}
