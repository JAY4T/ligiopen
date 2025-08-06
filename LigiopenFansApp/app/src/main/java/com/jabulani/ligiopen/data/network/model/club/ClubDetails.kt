package com.jabulani.ligiopen.data.network.model.club

import com.jabulani.ligiopen.data.network.model.player.PlayerDetails
import com.jabulani.ligiopen.data.network.model.file.FileData
import kotlinx.serialization.Serializable

@Serializable
data class ClubDetails(
    val clubId: Int,
    val clubLogo: FileData,
    val clubMainPhoto: FileData?,
    val name: String,
    val clubAbbreviation: String?,
    val description: String,
    val country: String,
    val county: String?,
    val town: String?,
    val favorite: Boolean,
    val division: ClubDivisionDt,
    val startedOn: String,
    val createdAt: String,
    val archived: Boolean,
    val archivedAt: String?,
    val players: List<PlayerDetails>
)
