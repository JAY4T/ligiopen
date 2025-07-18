package com.admin.ligiopen.data.network.models.user

import kotlinx.serialization.Serializable

@Serializable
data class UsersResponseBody(
    val statusCode: Int,
    val message: String,
    val data: List<UserDto>
)
