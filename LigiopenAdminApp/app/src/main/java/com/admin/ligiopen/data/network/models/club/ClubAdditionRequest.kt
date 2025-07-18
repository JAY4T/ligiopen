package com.admin.ligiopen.data.network.models.club

import kotlinx.serialization.Serializable

@Serializable
data class ClubAdditionRequest(
    val name: String,
    val description: String,
    val startedOn: String,
    val country: String,
    val county: String,
    val town: String,
)
