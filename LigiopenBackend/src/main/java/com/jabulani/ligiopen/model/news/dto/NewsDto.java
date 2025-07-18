package com.jabulani.ligiopen.model.news.dto;

import com.jabulani.ligiopen.model.aws.dto.FileDto;
import com.jabulani.ligiopen.model.news.NewsStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsDto {
    private Integer id;
    private FileDto coverPhoto;
    private String title;
    private String subTitle;
    private Boolean neutral;
    private NewsStatus newsStatus;
    private LocalDateTime publishedAt;
    private List<NewsItemDto> newsItems;
    private List<Integer> clubs;
}
