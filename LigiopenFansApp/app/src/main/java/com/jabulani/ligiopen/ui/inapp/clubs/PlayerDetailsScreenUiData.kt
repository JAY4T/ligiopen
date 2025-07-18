package com.jabulani.ligiopen.ui.inapp.clubs

import com.jabulani.ligiopen.data.db.model.UserAccount
import com.jabulani.ligiopen.data.db.model.variables.userAccountDt
import com.jabulani.ligiopen.data.network.model.club.ClubDetails
import com.jabulani.ligiopen.data.network.model.club.club
import com.jabulani.ligiopen.data.network.model.club.emptyClub
import com.jabulani.ligiopen.data.network.model.player.PlayerDetails
import com.jabulani.ligiopen.data.network.model.player.emptyPlayer
import com.jabulani.ligiopen.data.network.model.player.player

data class PlayerDetailsScreenUiData(
    val userAccount: UserAccount = userAccountDt,
    val playerDetails: PlayerDetails = emptyPlayer,
    val clubDetails: ClubDetails = emptyClub,
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL
)
