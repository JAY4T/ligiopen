package com.admin.ligiopen.data.network.models.club

import com.admin.ligiopen.data.network.models.file.FileData
import com.admin.ligiopen.data.network.models.player.PlayerData
import com.admin.ligiopen.data.network.models.user.UserDto
import kotlinx.serialization.Serializable

@Serializable
data class ClubData(
    val clubId: Int,
    val clubLogo: FileData,
    val clubMainPhoto: FileData?,
    val name: String,
    val clubAbbreviation: String?,
    val description: String,
    val country: String,
    val county: String?,
    val town: String?,
    val startedOn: String,
    val createdAt: String,
    val archived: Boolean,
    val archivedAt: String?,
    val players: List<PlayerData>,
    val clubStatus: String,
    val admins: List<UserDto>
)
