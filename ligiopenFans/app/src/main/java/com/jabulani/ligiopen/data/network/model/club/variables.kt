package com.jabulani.ligiopen.data.network.model.club

import com.jabulani.ligiopen.data.network.model.player.players
import com.jabulani.ligiopen.data.network.model.file.fileData

val division = ClubDivisionDt(
    id = 1,
    name = "Kenya Women Premier League"
)

val emptyDivision = ClubDivisionDt(
    id = 0,
    name = ""
)

val divisions = List(10) {
    ClubDivisionDt(
        id = 1,
        name = "Kenya Women Premier League"
    )
}

val club = ClubDetails(
    clubId = 1,
    clubLogo = fileData,
    clubMainPhoto = fileData,
    name = "AFC Leopards",
    clubAbbreviation = "AFC FC",
    description = "This is AFC Leopards team",
    country = "Kenya",
    county = "Kakamega",
    town = "Kakamega town",
    startedOn = "2020-09-17",
    createdAt = "2025-02-07T08:02:01.878284",
    archived = false,
    archivedAt = null,
    favorite = false,
    division = division,
    players = players
)

val emptyClub = ClubDetails(
    clubId = 0,
    clubLogo = fileData,
    clubMainPhoto = fileData,
    name = "",
    clubAbbreviation = "",
    description = "",
    country = "",
    county = "",
    town = "",
    startedOn = "",
    createdAt = "",
    archived = false,
    archivedAt = null,
    division = division,
    favorite = false,
    players = emptyList()
)

val clubs = List(10) {index ->
    ClubDetails(
        clubId = 1 + index,
        clubLogo = fileData,
        clubMainPhoto = fileData,
        name = "AFC Leopards",
        clubAbbreviation = "AFC FC",
        description = "This is AFC Leopards team",
        country = "Kenya",
        county = "Kakamega",
        town = "Kakamega town",
        startedOn = "2020-09-17",
        createdAt = "2025-02-07T08:02:01.878284",
        archived = false,
        archivedAt = null,
        division = division,
        favorite = false,
        players = players
    )
}

