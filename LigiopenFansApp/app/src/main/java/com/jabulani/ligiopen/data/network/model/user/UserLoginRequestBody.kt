package com.jabulani.ligiopen.data.network.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginRequestBody(
    val email: String,
    val password: String
)

