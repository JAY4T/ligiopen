package com.jabulani.ligiopen.controller.news;

import com.jabulani.ligiopen.config.response.BuildResponse;
import com.jabulani.ligiopen.config.response.Response;
import com.jabulani.ligiopen.model.news.dto.NewStatusUpdateDto;
import com.jabulani.ligiopen.model.news.dto.NewsCreationRequestDto;
import com.jabulani.ligiopen.model.news.dto.NewsItemCreationDto;
import com.jabulani.ligiopen.service.news.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/")
public class NewsControllerImpl implements NewsController{
    private final BuildResponse buildResponse;
    private final NewsService newsService;
    @Autowired
    public NewsControllerImpl(
            NewsService newsService,
            BuildResponse buildResponse
    ) {
        this.newsService = newsService;
        this.buildResponse = buildResponse;
    }
    @PostMapping("news")
    @Override
    public ResponseEntity<Response> createNews(
            @RequestPart("data") NewsCreationRequestDto newsCreationRequestDto,
            @RequestPart("file") MultipartFile coverPhoto
    ) throws IOException {
        if(coverPhoto == null) {
            return buildResponse.createResponse("null", null, "cover photo cannot be null", HttpStatus.BAD_REQUEST);
        }
        return buildResponse.createResponse("news", newsService.createNews(newsCreationRequestDto, coverPhoto), "News created", HttpStatus.CREATED);
    }

    @PutMapping("news/cover-photo-update/{newsId}")
    @Override
    public ResponseEntity<Response> updateNewsCoverPhoto(
            @PathVariable("newsId") Integer newsId,
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        if(file == null) {
            return buildResponse.createResponse("null", null, "file cannot be null", HttpStatus.BAD_REQUEST);
        }
        return buildResponse.createResponse("news", newsService.updateNewsCoverPhoto(newsId, file), "News cover photo updated", HttpStatus.OK);
    }
    @GetMapping("news/{newsId}")
    @Override
    public ResponseEntity<Response> getNews(@PathVariable("newsId") Integer newsId) {
        return buildResponse.createResponse("news", newsService.getNews(newsId), "News fetched", HttpStatus.OK);
    }
    @GetMapping("news/all")
    @Override
    public ResponseEntity<Response> getAllNews(
            @RequestParam(value = "clubId", required = false) Integer clubId,
            @RequestParam(value = "status", required = false) String status
    ) {
        return buildResponse.createResponse("news", newsService.getAllNews(clubId, status), "News fetched", HttpStatus.OK);
    }

    @DeleteMapping("news/delete/{newsId}")
    @Override
    public ResponseEntity<Response> deleteNews(
            @PathVariable("newsId") Integer newsId
    ) {
        return buildResponse.createResponse("news", newsService.deleteNews(newsId), "News deleted", HttpStatus.OK);
    }

    @DeleteMapping("news/news-cover-photo-delete/{fileId}")
    @Override
    public ResponseEntity<Response> deleteNewsCoverPhoto(
            @PathVariable("fileId") Integer fileId
    ) {
        return buildResponse.createResponse("news", newsService.deleteNewsCoverPhoto(fileId), "News cover photo deleted", HttpStatus.OK);
    }
    @PostMapping("news-item")
    @Override
    public ResponseEntity<Response> createNewsItem(
            @RequestPart("data") NewsItemCreationDto newsItemCreationDto,
            @RequestPart("file") MultipartFile file
    ) throws IOException {

        return buildResponse.createResponse("news item", newsService.createNewsItem(newsItemCreationDto, file), "News item created", HttpStatus.CREATED);
    }
    @GetMapping("news-item/{newsId}")
    @Override
    public ResponseEntity<Response> getNewsItem(
            @PathVariable("newsId") Integer newsItemId
    ) {
        return buildResponse.createResponse("news item", newsService.getNewsItem(newsItemId), "News item fetched", HttpStatus.OK);
    }

    @PutMapping("news/status-update")
    @Override
    public ResponseEntity<Response> updateNewsStatus(@RequestBody NewStatusUpdateDto newStatusUpdateDto) {
        return buildResponse.createResponse("news", newsService.updateNewsStatus(newStatusUpdateDto), "News status updated", HttpStatus.OK);
    }
}
