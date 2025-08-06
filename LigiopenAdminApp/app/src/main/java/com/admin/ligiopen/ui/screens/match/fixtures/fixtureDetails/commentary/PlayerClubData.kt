package com.admin.ligiopen.ui.screens.match.fixtures.fixtureDetails.commentary

import android.net.Uri

data class PlayerClubData(
    val playerId: Int = 0,
    val clubId: Int = 0,
    val name: String = "",
    val home: Boolean = false,
    val bench: Boolean = false,
    val clubLogo: String? = null,
)
