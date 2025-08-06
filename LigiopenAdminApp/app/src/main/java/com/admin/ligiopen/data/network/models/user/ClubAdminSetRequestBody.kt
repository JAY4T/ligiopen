package com.admin.ligiopen.data.network.models.user

import kotlinx.serialization.Serializable

@Serializable
data class ClubAdminSetRequestBody(
    val userId: Int,
    val teamId: Int
)
