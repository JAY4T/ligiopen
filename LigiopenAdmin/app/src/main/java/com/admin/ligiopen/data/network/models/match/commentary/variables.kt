package com.admin.ligiopen.data.network.models.match.commentary

import com.admin.ligiopen.data.network.models.club.club
import com.admin.ligiopen.data.network.models.match.events.MatchEventType
import com.admin.ligiopen.data.network.models.player.player

val commentary = MatchCommentaryData(
    matchCommentaryId = 0,
    postMatchAnalysisId = 0,
    files = emptyList(),
    minute = "22",
    createdAt = "2025-02-27T16:06:31.587932642",
    updatedAt = null,
    archivedAt = null,
    archived = false,
    matchEventType = MatchEventType.GOAL,
    cornerEvent = null,
    foulEvent = null,
    freeKickEvent = null,
    fullTimeEvent = null,
    halfTimeEvent = null,
    goalEvent = null,
    goalKickEvent = null,
    injuryEvent = null,
    kickOffEvent = null,
    substitutionEvent = null,
    offsideEvent = null,
    ownGoalEvent = null,
    penaltyEvent = null,
    throwInEvent = null,
    mainPlayer = player,
    secondaryPlayer = player,
    homeClub = club,
    awayClub = club
)

val matchCommentaries = List(10) {
    MatchCommentaryData(
        matchCommentaryId = 0,
        postMatchAnalysisId = 0,
        files = emptyList(),
        minute = "22",
        createdAt = "2025-02-27T16:06:31.587932642",
        updatedAt = null,
        archivedAt = null,
        archived = false,
        matchEventType = MatchEventType.GOAL,
        cornerEvent = null,
        foulEvent = null,
        freeKickEvent = null,
        fullTimeEvent = null,
        halfTimeEvent = null,
        goalEvent = null,
        goalKickEvent = null,
        injuryEvent = null,
        kickOffEvent = null,
        substitutionEvent = null,
        ownGoalEvent = null,
        offsideEvent = null,
        penaltyEvent = null,
        throwInEvent = null,
        mainPlayer = player,
        secondaryPlayer = player,
        homeClub = club,
        awayClub = club
    )
}