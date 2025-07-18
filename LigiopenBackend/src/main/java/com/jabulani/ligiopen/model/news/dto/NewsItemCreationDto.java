package com.jabulani.ligiopen.model.news.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsItemCreationDto {
    private String title;
    private String subtitle;
    private String paragraph;
    private Integer newsId;
}
