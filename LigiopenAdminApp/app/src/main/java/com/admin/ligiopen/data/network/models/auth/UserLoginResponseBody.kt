package com.admin.ligiopen.data.network.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginResponseBody(
    val statusCode: Int,
    val message: String,
    val data: UserLoginResponseBodyData
)




