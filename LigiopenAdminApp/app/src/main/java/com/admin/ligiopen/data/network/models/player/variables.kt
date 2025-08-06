package com.admin.ligiopen.data.network.models.player

import com.admin.ligiopen.data.network.models.file.emptyFileData
import com.admin.ligiopen.data.network.models.file.emptyFileDts
import com.admin.ligiopen.data.network.models.file.fileData
import com.admin.ligiopen.data.network.models.file.fileDts

val player = PlayerData(
    playerId = 2,
    mainPhoto = fileData,
    username = "Alexis M",
    number = 1,
    playerPosition = PlayerPosition.FORWARD,
    age = 22,
    height = 54.4,
    weight = 60.2,
    country = "Kenya",
    county = "Nairobi",
    town = "Nairobi",
    clubId = 1,
    files = fileDts,
    playerState = PlayerState.ACTIVE
)

val emptyPlayer = PlayerData(
    playerId = 1,
    mainPhoto = emptyFileData,
    username = "",
    number = 1,
    playerPosition = PlayerPosition.FORWARD,
    age = 1,
    height = 0.0,
    weight = 0.0,
    country = "",
    county = "",
    town = "",
    clubId = 1,
    files = emptyFileDts,
    playerState = PlayerState.ACTIVE
)

val players = List(10) { index ->
    PlayerData(
        playerId = 2 + index,
        mainPhoto = fileData,
        username = "Alexis M",
        number = 1 + index,
        playerPosition = PlayerPosition.FORWARD,
        age = 22,
        height = 54.4,
        weight = 60.2,
        country = "Kenya",
        county = "Nairobi",
        town = "Nairobi",
        clubId = 1,
        files = fileDts,
        playerState = PlayerState.ACTIVE
    )
}