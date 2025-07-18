package com.admin.ligiopen.data.network.models.news

import kotlinx.serialization.Serializable

@Serializable
data class SingleNewsResponseBody(
    val statusCode: Int,
    val message: String,
    val data: NewsDto
)
