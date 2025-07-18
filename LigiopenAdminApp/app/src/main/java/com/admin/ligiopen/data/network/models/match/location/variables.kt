package com.admin.ligiopen.data.network.models.match.location

import com.admin.ligiopen.data.network.models.file.fileDts

val matchLocation = MatchLocationData(
    locationId = 1,
    venueName = "Afraha stadium",
    country = "Kenya",
    county = "Nakuru",
    town = "Nakuru",
    photos = fileDts,
//    matchFixturesIds = listOf(1, 2)
)

val emptyLocation = MatchLocationData(
    locationId = 1,
    venueName = "",
    country = "",
    county = "",
    town = "",
    photos = emptyList(),
//    matchFixturesIds = emptyList()
)

val matchLocations = List(10) {
    MatchLocationData(
        locationId = 1,
        venueName = "Afraha stadium",
        country = "Kenya",
        county = "Nakuru",
        town = "Nakuru",
        photos = fileDts,
//        matchFixturesIds = listOf(1, 2)
    )
}