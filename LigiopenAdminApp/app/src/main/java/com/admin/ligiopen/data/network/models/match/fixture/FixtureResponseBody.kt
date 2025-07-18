package com.admin.ligiopen.data.network.models.match.fixture

import kotlinx.serialization.Serializable

@Serializable
data class FixtureResponseBody(
    val statusCode: Int,
    val message: String,
    val data: FixtureData
)
