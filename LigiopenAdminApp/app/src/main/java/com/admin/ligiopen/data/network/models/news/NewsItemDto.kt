package com.admin.ligiopen.data.network.models.news

import com.admin.ligiopen.data.network.models.file.FileData
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
