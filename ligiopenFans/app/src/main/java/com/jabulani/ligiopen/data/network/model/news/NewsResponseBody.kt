package com.jabulani.ligiopen.data.network.model.news

import kotlinx.serialization.Serializable

@Serializable
data class NewsResponseBody(
    val statusCode: Int,
    val message: String,
    val data: List<NewsDto>
)
