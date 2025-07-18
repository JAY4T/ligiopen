package com.admin.ligiopen.data.network.models.match.fixture

import kotlinx.serialization.Serializable

@Serializable
data class FixtureCreationRequest(
    val homeClub: Int,
    val awayClub: Int,
    val matchDateTime: String,
    val matchStatus: MatchStatus,
    val locationId: Int
)
