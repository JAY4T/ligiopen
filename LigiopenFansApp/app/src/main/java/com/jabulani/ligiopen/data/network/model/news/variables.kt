package com.jabulani.ligiopen.data.network.model.news

import com.jabulani.ligiopen.data.network.model.file.fileData

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
        clubs = mutableListOf(1, 2)
    )
}