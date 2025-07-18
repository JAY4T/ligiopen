package com.admin.ligiopen.data.network.models.file

import kotlinx.serialization.Serializable

@Serializable
data class FileData(
    val fileId: Int,
    val link: String,
)
