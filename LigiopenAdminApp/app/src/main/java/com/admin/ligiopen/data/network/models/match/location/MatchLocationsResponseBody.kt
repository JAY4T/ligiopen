package com.admin.ligiopen.data.network.models.match.location

import kotlinx.serialization.Serializable

@Serializable
data class MatchLocationsResponseBody(
    val statusCode: Int,
    val message: String,
    val data: List<MatchLocationData>
)
