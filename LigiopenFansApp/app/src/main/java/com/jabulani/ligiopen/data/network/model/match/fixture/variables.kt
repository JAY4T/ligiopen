package com.jabulani.ligiopen.data.network.model.match.fixture

import com.jabulani.ligiopen.data.network.model.club.club

val fixture = FixtureData(
    matchFixtureId = 0,
    matchLocationId = 1,
    postMatchAnalysisId = 1,
    homeClub = club,
    awayClub = club,
    awayClubScore = 0,
    homeClubScore = 0,
    createdAt = "2025-02-07T08:02:01.878284",
    matchDateTime = "2025-02-07T08:02:01.878284",
    matchStatus = MatchStatus.PENDING,
    cancellationReason = null
)

val fixtures = List(10) {
    FixtureData(
        matchFixtureId = 0 + it,
        matchLocationId = 1 + it,
        postMatchAnalysisId = 1 + it,
        homeClub = club,
        awayClub = club,
        awayClubScore = 0,
        homeClubScore = 0,
        createdAt = "2025-02-07T08:02:01.878284",
        matchDateTime = "2025-02-07T08:02:01.878284",
        matchStatus = MatchStatus.PENDING,
        cancellationReason = null
    )
}