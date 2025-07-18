package com.jabulani.ligiopen.dao.news;

import com.jabulani.ligiopen.model.club.entity.Club;
import com.jabulani.ligiopen.model.news.News;
import com.jabulani.ligiopen.model.news.NewsItem;
import com.jabulani.ligiopen.model.news.NewsStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class NewsDaoImpl implements NewsDao{
    private final EntityManager entityManager;
    @Autowired
    public NewsDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public News createNews(News news) {
        entityManager.persist(news);
        return news;
    }

    @Override
    public News updateNews(News news) {
        entityManager.merge(news);
        return news;
    }

    @Override
    public String deleteNews(Integer newsId) {
        return null;
    }

    @Override
    public News getNewsById(Integer newsId) {
        TypedQuery<News> query = entityManager.createQuery("from News where id = :newsId", News.class);
        query.setParameter("newsId", newsId);
        return query.getSingleResult();
    }

    @Override
    public List<News> getAllNews(Integer clubId, String status) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<News> cq = cb.createQuery(News.class);
        Root<News> newsRoot = cq.from(News.class);
        cq.select(newsRoot).distinct(true);

        List<Predicate> predicates = new ArrayList<>();

        if (clubId != null) {
            Join<News, Club> clubJoin = newsRoot.join("clubs", JoinType.INNER);
            predicates.add(cb.equal(clubJoin.get("id"), clubId));
        }

        if (status != null && !status.isBlank()) {
            try {
                NewsStatus newsStatusEnum = NewsStatus.valueOf(status.toUpperCase());
                predicates.add(cb.equal(newsRoot.get("newsStatus"), newsStatusEnum));
            } catch (IllegalArgumentException e) {
                // Optional: log invalid status or ignore silently
            }
        }

        cq.where(predicates.toArray(new Predicate[0]));
        TypedQuery<News> query = entityManager.createQuery(cq);
        return query.getResultList();
    }


    @Override
    public NewsItem createNewsItem(NewsItem newsItem) {
        entityManager.persist(newsItem);
        return newsItem;
    }

    @Override
    public NewsItem updateNewsItem(NewsItem newsItem) {
        entityManager.merge(newsItem);
        return newsItem;
    }

    @Override
    public String deleteNewsItem(Integer newsItemId) {
        return null;
    }

    @Override
    public NewsItem getNewsItemById(Integer newsItemId) {
        TypedQuery<NewsItem> query = entityManager.createQuery("from NewsItem where id = :newItemId", NewsItem.class);
        query.setParameter("newItemId", newsItemId);
        return query.getSingleResult();
    }

    @Override
    public List<NewsItem> getAllNewsItems() {
        TypedQuery<NewsItem> query = entityManager.createQuery("from NewsItem ", NewsItem.class);
        return query.getResultList();
    }
}
