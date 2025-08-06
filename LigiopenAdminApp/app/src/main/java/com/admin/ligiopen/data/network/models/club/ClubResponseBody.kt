package com.admin.ligiopen.data.network.models.club

import kotlinx.serialization.Serializable

@Serializable
data class ClubResponseBody(
    val statusCode: Int,
    val message: String,
    val data: ClubData
)
