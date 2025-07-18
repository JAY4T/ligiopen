package com.admin.ligiopen.data.network.models.match.events

import kotlinx.serialization.Serializable

@Serializable
data class EventCreationRequest(
    val title: String,
    val summary: String,
    val minute: String,
    val matchEventType: MatchEventType,
    val mainPlayerId: Int?,
    val secondaryPlayerId: Int?,
    val fouledPlayerId: Int?,
    val isYellowCard: Boolean?,
    val isRedCard: Boolean?,
    val isScored: Boolean?
)
