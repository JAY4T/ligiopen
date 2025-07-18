package com.jabulani.ligiopen.model.news.dto;

import com.jabulani.ligiopen.model.news.NewsStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewStatusUpdateDto {
    private Integer newsId;
    private NewsStatus newsStatus;
}
