package com.admin.ligiopen.data.network.models.club

import kotlinx.serialization.Serializable

@Serializable
data class ClubUpdateRequest(
    val clubId: Int,
    val divisionId: Int?,
    val homeId: Int?,
    val name: String,
    val clubAbbreviation: String,
    val description: String,
    val startedOn: String,
    val country: String,
    val county: String,
    val town: String,
)
