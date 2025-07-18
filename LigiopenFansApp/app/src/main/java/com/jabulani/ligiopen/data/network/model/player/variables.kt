package com.jabulani.ligiopen.data.network.model.player

import com.jabulani.ligiopen.data.network.model.file.fileData
import com.jabulani.ligiopen.data.network.model.file.fileDts

val player = PlayerDetails(
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

val emptyPlayer = PlayerDetails(
    playerId = 0,
    mainPhoto = fileData,
    username = "",
    number = 0,
    playerPosition = PlayerPosition.FORWARD,
    age = 0,
    height = 0.0,
    weight = 0.0,
    country = "",
    county = "",
    town = "",
    clubId = 1,
    files = fileDts,
    playerState = PlayerState.ACTIVE
)

val players = List(10) { index ->
    PlayerDetails(
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