package com.admin.ligiopen.data.network.models.club

import kotlinx.serialization.Serializable

@Serializable
data class ChangeClubStatusResponseBody(
    val statusCode: Int,
    val message: String,
    val data: ClubMinDetails,
)
