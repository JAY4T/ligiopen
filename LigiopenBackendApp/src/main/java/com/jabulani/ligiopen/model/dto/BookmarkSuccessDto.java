package com.jabulani.ligiopen.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookmarkSuccessDto {
    private Boolean success;
    private Integer clubId;
}
