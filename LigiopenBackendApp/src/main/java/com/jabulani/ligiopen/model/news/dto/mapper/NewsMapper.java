package com.jabulani.ligiopen.model.news.dto.mapper;

import com.jabulani.ligiopen.model.aws.dto.mapper.FileMapper;
import com.jabulani.ligiopen.model.club.entity.Club;
import com.jabulani.ligiopen.model.news.News;
import com.jabulani.ligiopen.model.news.NewsItem;
import com.jabulani.ligiopen.model.news.dto.NewsDto;
import com.jabulani.ligiopen.model.news.dto.NewsItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NewsMapper {
    private final FileMapper fileMapper;
    @Autowired
    public NewsMapper(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }
    public NewsDto newsDto(News news) {
        List<NewsItem> newsItemList = new ArrayList<>();

        if(news.getNewsItems() != null && !news.getNewsItems().isEmpty()) {
            newsItemList.addAll(news.getNewsItems());
        }

        return NewsDto.builder()
                .id(news.getId())
                .coverPhoto(fileMapper.fileDto(news.getCoverPhoto()))
                .title(news.getTitle())
                .subTitle(news.getSubTitle())
                .neutral(news.getNeutral())
                .publishedAt(news.getPublishedAt())
                .newsStatus(news.getNewsStatus())
                .newsItems(newsItemList.stream().map(this::newsItemDto).collect(Collectors.toList()))
                .clubs(news.getClubs().stream().map(Club::getId).collect(Collectors.toList()))
                .build();
    }

    public NewsItemDto newsItemDto(NewsItem newsItem) {
        return NewsItemDto.builder()
                .id(newsItem.getId())
                .title(newsItem.getTitle())
                .subTitle(newsItem.getSubTitle())
                .paragraph(newsItem.getParagraph())
                .file(fileMapper.fileDto(newsItem.getFile()))
                .newsId(newsItem.getId())
                .build();

    }
}
