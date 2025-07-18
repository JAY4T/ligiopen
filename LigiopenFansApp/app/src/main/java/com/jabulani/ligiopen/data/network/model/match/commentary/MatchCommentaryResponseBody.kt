package com.jabulani.ligiopen.data.network.model.match.commentary

import kotlinx.serialization.Serializable

@Serializable
data class MatchCommentaryResponseBody(
    val statusCode: Int,
    val message: String,
    val data: MatchCommentaryData
)
