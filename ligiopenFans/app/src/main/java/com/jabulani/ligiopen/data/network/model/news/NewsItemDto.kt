package com.jabulani.ligiopen.data.network.model.news

import com.jabulani.ligiopen.data.network.model.file.FileData
import kotlinx.serialization.Serializable

@Serializable
data class NewsItemDto(
    val id: Int,
    val title: String?,
    val subTitle: String?,
    val paragraph: String,
    val file: FileData?,
    val newsId: Int
)
