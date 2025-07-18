package com.jabulani.ligiopen.data.network.model.location

import com.jabulani.ligiopen.data.network.model.file.FileData
import kotlinx.serialization.Serializable

@Serializable
data class MatchLocationData(
    val locationId: Int,
    val venueName: String,
    val country: String,
    val county: String,
    val town: String,
    val photos: List<FileData>?,
    val matchFixturesIds: List<Int>?
)
