package com.admin.ligiopen.data.network.models.match.commentary

import kotlinx.serialization.Serializable

@Serializable
data class FullMatchResponseBody(
    val statusCode: Int,
    val message: String,
    val data: FullMatchData
)
