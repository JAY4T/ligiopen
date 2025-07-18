package com.jabulani.ligiopen.data.network.model.match.commentary

import kotlinx.serialization.Serializable

@Serializable
data class FullMatchData(
    val id: Int,
    val marchFixtureId: Int,
    val homeClubScore: Int,
    val awayClubScore: Int,
    val manOfTheMatchId: Int?,
    val minuteByMinuteCommentary: List<MatchCommentaryData>
)
