package com.admin.ligiopen.data.network.models.match.fixture

import kotlinx.serialization.Serializable

@Serializable
data class FixtureUpdateRequest(
    val matchFixtureId: Int,
    val homeClub: Int,
    val awayClub: Int,
    val matchDateTime: String,
    val matchStatus: MatchStatus,
    val locationId: Int
)
