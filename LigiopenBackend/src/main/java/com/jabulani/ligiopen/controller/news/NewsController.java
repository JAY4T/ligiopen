package com.jabulani.ligiopen.controller.news;

import com.jabulani.ligiopen.config.response.Response;
import com.jabulani.ligiopen.model.news.dto.NewStatusUpdateDto;
import com.jabulani.ligiopen.model.news.dto.NewsCreationRequestDto;
import com.jabulani.ligiopen.model.news.dto.NewsDto;
import com.jabulani.ligiopen.model.news.dto.NewsItemCreationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface NewsController {
    ResponseEntity<Response> createNews(NewsCreationRequestDto newsCreationRequestDto, MultipartFile coverPhoto) throws IOException;
//    NewsDto updateNews(NewsCreationRequestDto newsCreationRequestDto);
    ResponseEntity<Response> updateNewsCoverPhoto(Integer newsId, MultipartFile file) throws IOException;
    ResponseEntity<Response> getNews(Integer newsId);
    ResponseEntity<Response> getAllNews(Integer clubId, String status);
    ResponseEntity<Response> deleteNews(Integer newsId);
    ResponseEntity<Response> deleteNewsCoverPhoto(Integer fileId);

    ResponseEntity<Response> createNewsItem(NewsItemCreationDto newsItemCreationDto, MultipartFile file) throws IOException;
//    NewsItemDto updateNewsItem(NewsItemCreationDto newsItemCreationDto);

    ResponseEntity<Response> getNewsItem(Integer newsItemId);

    ResponseEntity<Response> updateNewsStatus(NewStatusUpdateDto newStatusUpdateDto);
}
