package com.admin.ligiopen.data.network.models.match.location

import com.admin.ligiopen.data.network.models.file.FileData
import kotlinx.serialization.Serializable

@Serializable
data class MatchLocationData(
    val locationId: Int,
    val venueName: String,
    val country: String?,
    val county: String?,
    val town: String?,
    val photos: List<FileData>?,
//    val matchFixturesIds: List<Int>?
)
