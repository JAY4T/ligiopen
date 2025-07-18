package com.jabulani.ligiopen.data.network.model.location

import com.jabulani.ligiopen.data.network.model.file.fileDts

val matchLocation = MatchLocationData(
    locationId = 1,
    venueName = "Afraha stadium",
    country = "Kenya",
    county = "Nakuru",
    town = "Nakuru",
    photos = fileDts,
    matchFixturesIds = listOf(1, 2)
)

val matchLocations = List(10) {
    MatchLocationData(
        locationId = 1,
        venueName = "Afraha stadium",
        country = "Kenya",
        county = "Nakuru",
        town = "Nakuru",
        photos = fileDts,
        matchFixturesIds = listOf(1, 2)
    )
}