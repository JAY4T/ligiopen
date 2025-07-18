package com.admin.ligiopen.data.network.models.user

import kotlinx.serialization.Serializable

@Serializable
data class AdminSetRequestBody(
    val userId: Int
)
