package com.jabulani.ligiopen.model.match.entity.events;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Data
@NoArgsConstructor(force = true)
//@AllArgsConstructor
//@Builder
@EqualsAndHashCode(callSuper = true)
@Table(name = "throw_in_event")
public class ThrowInEvent extends MatchEvent {
}
