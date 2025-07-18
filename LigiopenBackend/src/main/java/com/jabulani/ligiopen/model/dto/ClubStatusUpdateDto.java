package com.jabulani.ligiopen.model.dto;

import com.jabulani.ligiopen.model.club.entity.ClubStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubStatusUpdateDto {
    private Integer clubId;
    private ClubStatus clubStatus;
}
