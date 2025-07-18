package com.admin.ligiopen.data.network.models.player

import kotlinx.serialization.Serializable

@Serializable
data class PlayersResponseBody(
    val statusCode: Int,
    val message: String,
    val data: List<PlayerData>
)
