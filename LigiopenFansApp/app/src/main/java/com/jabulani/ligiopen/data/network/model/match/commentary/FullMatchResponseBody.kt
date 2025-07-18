package com.jabulani.ligiopen.data.network.model.match.commentary

import com.jabulani.ligiopen.data.network.model.match.commentary.FullMatchData
import kotlinx.serialization.Serializable

@Serializable
data class FullMatchResponseBody(
    val statusCode: Int,
    val message: String,
    val data: FullMatchData
)
