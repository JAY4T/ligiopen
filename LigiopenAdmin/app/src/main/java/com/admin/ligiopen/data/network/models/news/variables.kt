package com.admin.ligiopen.data.network.models.news

import com.admin.ligiopen.data.network.models.file.fileData

val singleNewsItem = NewsItemDto(
    id = 0,
    title = "Paragraph 1",
    subTitle = "Paragraph 1",
    paragraph = "Paragraph 1 text",
    file = fileData,
    newsId = 0
)

val newsItems = List(10) {
    NewsItemDto(
        id = 0,
        title = "Paragraph 1",
        subTitle = "Paragraph 1",
        paragraph = "Paragraph 1 text",
        file = fileData,
        newsId = 0
    )
}

val singleNews = NewsDto(
    id = 0,
    coverPhoto = fileData,
    title = "News Title",
    subTitle = "News Sub Title",
    neutral = false,
    newsItems = newsItems,
    newsStatus = "APPROVED",
    publishedAt = "2025-05-30T11:01:38.78474",
    clubs = mutableListOf(1, 2)
)

val news = List(10) {
    NewsDto(
        id = 0,
        coverPhoto = fileData,
        title = "News Title",
        subTitle = "News Sub Title",
        neutral = false,
        newsItems = newsItems,
        newsStatus = "APPROVED",
        publishedAt = "2025-05-30T11:01:38.78474",
        clubs = mutableListOf(1, 2)
    )
}
