package com.admin.ligiopen.data.network.models.club

import kotlinx.serialization.Serializable

@Serializable
data class ClubsResponseBody(
    val statusCode: Int,
    val message: String,
    val data: List<ClubData>
)
