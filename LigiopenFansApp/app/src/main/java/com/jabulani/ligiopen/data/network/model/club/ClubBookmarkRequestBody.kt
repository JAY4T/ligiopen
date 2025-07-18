package com.jabulani.ligiopen.data.network.model.club

import kotlinx.serialization.Serializable

@Serializable
data class ClubBookmarkRequestBody(
    val userId: Int,
    val clubId: Int,
)
