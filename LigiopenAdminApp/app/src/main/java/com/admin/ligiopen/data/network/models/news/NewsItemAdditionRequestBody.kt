package com.admin.ligiopen.data.network.models.news

import kotlinx.serialization.Serializable

@Serializable
data class NewsItemAdditionRequestBody(
    val title: String,
    val subtitle: String,
    val paragraph: String,
    val newsId: Int
)
