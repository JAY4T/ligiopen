package com.jabulani.ligiopen.data.network.model.club

import kotlinx.serialization.Serializable

@Serializable
data class ClubBookmarkSuccessDt(
    val success: Boolean,
    val clubId: Int,
)
