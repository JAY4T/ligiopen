package com.admin.ligiopen.data.network.models.auth

import kotlinx.serialization.Serializable

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