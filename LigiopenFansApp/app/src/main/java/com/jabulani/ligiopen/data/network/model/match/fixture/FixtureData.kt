package com.jabulani.ligiopen.data.network.model.match.fixture

import com.jabulani.ligiopen.data.network.model.club.ClubDetails
import kotlinx.serialization.Serializable

@Serializable
data class FixtureData(
    val matchFixtureId: Int,
    val matchLocationId: Int,
    val postMatchAnalysisId: Int,
    val homeClub: ClubDetails,
    val awayClub: ClubDetails,
    val homeClubScore: Int?,
    val awayClubScore: Int?,
    val createdAt: String,
    val matchDateTime: String,
    val matchStatus: MatchStatus,
    val cancellationReason: String?
)
