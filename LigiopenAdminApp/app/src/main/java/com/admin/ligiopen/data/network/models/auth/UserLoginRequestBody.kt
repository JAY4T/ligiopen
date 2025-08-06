package com.admin.ligiopen.data.network.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginRequestBody(
    val email: String,
    val password: String
)

