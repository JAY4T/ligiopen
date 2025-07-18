package com.admin.ligiopen.data.network.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginResponseBodyData(
    val user: UserLoginResponseBodyDataUser,
    val token: String
)