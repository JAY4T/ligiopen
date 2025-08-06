package com.admin.ligiopen.data.network.models.club

import com.admin.ligiopen.data.network.models.file.FileData
import kotlinx.serialization.Serializable

@Serializable
data class ClubMinDetails(
    val clubId: Int,
    val clubName: String,
    val leagueName: String,
    val clubLogo: FileData,
    val clubStatus: String
)
