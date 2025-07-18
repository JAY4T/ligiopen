package com.jabulani.ligiopen.data.network.model.match.fixture

import kotlinx.serialization.Serializable

@Serializable
data class FixturesResponseBody(
    val statusCode: Int,
    val message: String,
    val data: List<FixtureData>
)
