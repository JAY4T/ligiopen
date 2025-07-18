package com.admin.ligiopen.data.network.models.news

import kotlinx.serialization.Serializable

@Serializable
data class NewsStatusUpdateRequestBody(
    val newsId: Int,
    val newsStatus: String
)
