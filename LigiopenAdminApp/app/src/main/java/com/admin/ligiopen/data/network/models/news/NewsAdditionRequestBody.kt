package com.admin.ligiopen.data.network.models.news

import kotlinx.serialization.Serializable

@Serializable
data class NewsAdditionRequestBody(
    val title: String,
    val subTitle: String,
    val teamsInvolved: List<Int>
)
