package com.jabulani.ligiopen.data.network.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginResponseBody(
    val statusCode: Int,
    val message: String,
    val data: UserLoginResponseBodyData
)

@Serializable
data class UserLoginResponseBodyData(
    val user: UserLoginResponseBodyDataUser,
    val token: String
)

@Serializable
data class UserLoginResponseBodyDataUser (
    val id: Int,
    val username: String,
    val email: String,
    val role: String,
    val createdAt: String,
    val archived: Boolean?,
    val archivedAt: String?
)
