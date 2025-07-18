package com.jabulani.ligiopen.model.news.dto;

import com.jabulani.ligiopen.model.aws.dto.FileDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsItemDto {
    private Integer id;
    private String title;
    private String subTitle;
    private String paragraph;
    private FileDto file;
    private Integer newsId;
}
