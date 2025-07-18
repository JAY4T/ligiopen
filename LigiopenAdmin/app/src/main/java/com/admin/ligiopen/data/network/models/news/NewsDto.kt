package com.admin.ligiopen.data.network.models.news

import com.admin.ligiopen.data.network.models.file.FileData
import kotlinx.serialization.Serializable

@Serializable
data class NewsDto(
    val id: Int,
    val coverPhoto: FileData,
    val title: String,
    val subTitle: String?,
    val neutral: Boolean,
    val newsStatus: String,
    val publishedAt: String,
    val newsItems: List<NewsItemDto>,
    val clubs: List<Int>
)
