package com.admin.ligiopen.data.network.models.match.location

import com.jabulani.ligiopen.data.network.model.location.MatchLocationData
import kotlinx.serialization.Serializable

@Serializable
data class MatchLocationsResponseBody(
    val statusCode: Int,
    val message: String,
    val data: List<MatchLocationData>
)
