package com.jabulani.ligiopen.data.network.model.news

import kotlinx.serialization.Serializable

@Serializable
data class SingleNewsResponseBody(
    val statusCode: Int,
    val message: String,
    val data: NewsDto
)
