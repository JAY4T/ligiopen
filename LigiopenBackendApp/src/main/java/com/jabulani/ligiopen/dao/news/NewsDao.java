package com.jabulani.ligiopen.dao.news;

import com.jabulani.ligiopen.model.news.News;
import com.jabulani.ligiopen.model.news.NewsItem;

import java.util.List;

public interface NewsDao {
    News createNews(News news);
    News updateNews(News news);
    String deleteNews(Integer newsId);
    News getNewsById(Integer newsId);
    List<News> getAllNews(Integer clubId, String status);

    NewsItem createNewsItem(NewsItem newsItem);
    NewsItem updateNewsItem(NewsItem newsItem);
    String deleteNewsItem(Integer newsItemId);
    NewsItem getNewsItemById(Integer newsItemId);
    List<NewsItem> getAllNewsItems();
}
