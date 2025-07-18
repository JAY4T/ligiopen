package com.jabulani.ligiopen.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerClubDto {
    private Integer id;
    private Integer playerId;
    private Integer clubId;
    private LocalDateTime createdAt;
    private Boolean archived;
    private LocalDateTime archivedAt;
}
