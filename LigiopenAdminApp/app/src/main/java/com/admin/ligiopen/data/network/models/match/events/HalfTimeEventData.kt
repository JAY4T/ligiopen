package com.admin.ligiopen.data.network.models.match.events

import com.admin.ligiopen.data.network.models.file.FileData
import kotlinx.serialization.Serializable

@Serializable
data class HalfTimeEventData(
    val title: String,
    val summary: String,
    val minute: String,
    val matchEventType: MatchEventType,
    val mainPlayerId: Int?,
    val files: List<FileData>,
    val awayClubScore: Int?,
    val homeClubScore: Int?,
)
