package com.jabulani.ligiopen.model.news.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsCreationRequestDto {
    private String title;
    private String subTitle;
    private List<Integer> teamsInvolved;
}
