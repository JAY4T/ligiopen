package com.admin.ligiopen.data.network.models.player

import kotlinx.serialization.Serializable

@Serializable
data class PlayerUpdateRequest(
    val playerId: Int,
    val username: String,
    val age: Int,
    val height: Double,
    val weight: Double,
    val number: Int,
    val playerPosition: String,
    val country: String,
    val county: String,
    val town: String
)
