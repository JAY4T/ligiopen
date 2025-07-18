package com.jabulani.ligiopen.service.news;

import com.jabulani.ligiopen.config.Constants;
import com.jabulani.ligiopen.dao.club.ClubDao;
import com.jabulani.ligiopen.dao.fileDao.FileDao;
import com.jabulani.ligiopen.dao.news.NewsDao;
import com.jabulani.ligiopen.model.aws.File;
import com.jabulani.ligiopen.model.club.entity.Club;
import com.jabulani.ligiopen.model.news.News;
import com.jabulani.ligiopen.model.news.NewsItem;
import com.jabulani.ligiopen.model.news.NewsStatus;
import com.jabulani.ligiopen.model.news.dto.*;
import com.jabulani.ligiopen.model.news.dto.mapper.NewsMapper;
import com.jabulani.ligiopen.service.aws.AwsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsServiceImpl implements NewsService{
    private final NewsMapper newsMapper;
    private final NewsDao newsDao;
    private final AwsService awsService;
    private final FileDao fileDao;

    private final ClubDao clubDao;
    @Autowired
    public NewsServiceImpl(
            NewsMapper newsMapper,
            NewsDao newsDao,
            AwsService awsService,
            ClubDao clubDao,
            FileDao fileDao
    ) {
        this.newsMapper = newsMapper;
        this.newsDao = newsDao;
        this.awsService = awsService;
        this.clubDao = clubDao;
        this.fileDao = fileDao;
    }
    @Transactional
    @Override
    public NewsDto createNews(NewsCreationRequestDto newsCreationRequestDto, MultipartFile coverPhoto) throws IOException {
        File cover = File.builder()
                .name(awsService.uploadFile(Constants.BUCKET_NAME, coverPhoto))
                .build();

        News news = News.builder()
                .title(newsCreationRequestDto.getTitle())
                .subTitle(newsCreationRequestDto.getSubTitle())
                .coverPhoto(cover)
                .neutral(true)
                .newsStatus(NewsStatus.PENDING)
                .publishedAt(LocalDateTime.now())
                .build();

        // Persist News first
        news = newsDao.createNews(news);

        List<Club> clubs = new ArrayList<>();
        boolean neutral = true;

        if(!newsCreationRequestDto.getTeamsInvolved().isEmpty()) {
            neutral = false;
            for(Integer id : newsCreationRequestDto.getTeamsInvolved()) {
                Club club = clubDao.getClubById(id);
                club.getNews().add(news);
                clubDao.updateClub(club);
                clubs.add(club);
            }
        }

        news.setNeutral(neutral);
        news.setClubs(clubs);
        newsDao.updateNews(news); // Optional

        return newsMapper.newsDto(news);
    }

    @Transactional
    @Override
    public NewsDto updateNews(NewsCreationRequestDto newsCreationRequestDto) {
        return null;
    }
    @Transactional
    @Override
    public NewsDto updateNewsCoverPhoto(Integer newsId, MultipartFile file) throws IOException {
        News news = newsDao.getNewsById(newsId);
        File currentCoverPhoto = news.getCoverPhoto();
//        fileDao.deleteFile(currentCoverPhoto.getId());

        File newCoverPhoto = File.builder()
                .name(awsService.uploadFile(Constants.BUCKET_NAME, file))
                .build();

        news.setCoverPhoto(newCoverPhoto);

        return newsMapper.newsDto(newsDao.updateNews(news));
    }
    @Transactional
    @Override
    public NewsDto getNews(Integer newsId) {
        return newsMapper.newsDto(newsDao.getNewsById(newsId));
    }

    @Transactional
    @Override
    public List<NewsDto> getAllNews(Integer clubId, String status) {
        return newsDao.getAllNews(clubId, status).stream().map(newsMapper::newsDto).collect(Collectors.toList());
    }

    @Override
    public String deleteNews(Integer newsId) {
        return null;
    }
    @Transactional
    @Override
    public String deleteNewsCoverPhoto(Integer fileId) {
        fileDao.deleteFile(fileId);
        return "News cover photo deleted";
    }
    @Transactional
    @Override
    public NewsItemDto createNewsItem(NewsItemCreationDto newsItemCreationDto, MultipartFile file) throws IOException {

        File newsItemFile = null;

        if(file != null) {
            newsItemFile = File.builder()
                    .name(awsService.uploadFile(Constants.BUCKET_NAME, file))
                    .build();
        }

        News news = newsDao.getNewsById(newsItemCreationDto.getNewsId());

        NewsItem newsItem = NewsItem.builder()
                .title(newsItemCreationDto.getTitle())
                .subTitle(newsItemCreationDto.getSubtitle())
                .paragraph(newsItemCreationDto.getParagraph())
                .file(newsItemFile)
                .news(news)
                .build();

        return newsMapper.newsItemDto(newsDao.createNewsItem(newsItem));
    }
    @Transactional
    @Override
    public NewsItemDto updateNewsItem(NewsItemCreationDto newsItemCreationDto) {
        return null;
    }
    @Transactional
    @Override
    public NewsItemDto getNewsItem(Integer newsItemId) {
        return newsMapper.newsItemDto(newsDao.getNewsItemById(newsItemId));
    }

    @Transactional
    @Override
    public NewsDto updateNewsStatus(NewStatusUpdateDto newStatusUpdateDto) {
        News news = newsDao.getNewsById(newStatusUpdateDto.getNewsId());
        news.setNewsStatus(newStatusUpdateDto.getNewsStatus());
        return newsMapper.newsDto(newsDao.updateNews(news));
    }
}
