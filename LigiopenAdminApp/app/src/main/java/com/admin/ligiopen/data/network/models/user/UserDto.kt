package com.admin.ligiopen.data.network.models.user

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int,
    val username: String,
    val email: String,
    val role: String,
    val createdAt: String,
    val archived: Boolean,
    val archivedAt: String?,
    val administeringClubId: Int?
)
