package com.jabulani.ligiopen.data.network.model.player

import kotlinx.serialization.Serializable

@Serializable
data class PlayersResponseBody(
    val statusCode: Int,
    val message: String,
    val data: List<PlayerDetails>
)
