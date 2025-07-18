package com.jabulani.ligiopen.data.network.model.club

import kotlinx.serialization.Serializable

@Serializable
data class ClubsResponseBody(
    val statusCode: Int,
    val message: String,
    val data: List<ClubDetails>
)
