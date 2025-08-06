package com.admin.ligiopen.data.network.models.club

import kotlinx.serialization.Serializable

@Serializable
data class ChangeClubStatusRequestBody(
    val clubId: Int,
    val clubStatus: String
)
