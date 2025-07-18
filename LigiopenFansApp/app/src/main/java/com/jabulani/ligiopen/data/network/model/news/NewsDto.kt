package com.jabulani.ligiopen.data.network.model.news

import com.jabulani.ligiopen.data.network.model.file.FileData
import kotlinx.serialization.Serializable

@Serializable
data class NewsDto(
    val id: Int,
    val coverPhoto: FileData,
    val title: String,
    val subTitle: String?,
    val neutral: Boolean,
    val newsItems: List<NewsItemDto>,
    val clubs: List<Int>
)
