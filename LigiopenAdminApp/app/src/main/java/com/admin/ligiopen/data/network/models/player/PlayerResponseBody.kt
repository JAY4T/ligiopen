package com.admin.ligiopen.data.network.models.player

import kotlinx.serialization.Serializable

@Serializable
data class PlayerResponseBody(
    val statusCode: Int,
    val message: String,
    val data: PlayerData
)
