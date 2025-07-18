package com.jabulani.ligiopen.data.network.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UserRegistrationResponseBody(
    val statusCode: Int,
    val message: String,
    val data: UserRegistrationResponseBodyData
)

@Serializable
data class UserRegistrationResponseBodyData(
    val id: Int,
    val username: String,
    val email: String,
    val role: String,
    val createdAt: String,
    val archived: Boolean?,
    val archivedAt: String?
)