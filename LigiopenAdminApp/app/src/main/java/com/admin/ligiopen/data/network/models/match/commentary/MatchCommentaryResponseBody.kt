package com.admin.ligiopen.data.network.models.match.commentary

import kotlinx.serialization.Serializable

@Serializable
data class MatchCommentaryResponseBody(
    val statusCode: Int,
    val message: String,
    val data: MatchCommentaryData
)
