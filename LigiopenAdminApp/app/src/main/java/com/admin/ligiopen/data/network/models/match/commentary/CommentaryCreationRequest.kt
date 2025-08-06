package com.admin.ligiopen.data.network.models.match.commentary

import com.admin.ligiopen.data.network.models.match.events.EventCreationRequest
import kotlinx.serialization.Serializable

@Serializable
data class CommentaryCreationRequest(
    val postMatchAnalysisId: Int,
    val minute: String,
    val matchEvent: EventCreationRequest
)


