package com.jabulani.ligiopen.data.network.model.file

import kotlinx.serialization.Serializable

@Serializable
data class FileData(
    val fileId: Int,
    val link: String,
)
