package com.admin.ligiopen.ui.screens.player

import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.player.PlayerData
import com.admin.ligiopen.data.network.models.player.emptyPlayer
import com.admin.ligiopen.data.room.db.userAccountDt
import com.admin.ligiopen.data.room.models.UserAccount

data class PlayerDetailsUiData(
    val userAccount: UserAccount = userAccountDt,
    val player: PlayerData = emptyPlayer,
    val username: String = "",
    val age: String = "",
    val height: String = "",
    val weight: String = "",
    val number: String = "",
    val playerPosition: String = "",
    val country: String = "",
    val county: String = "",
    val town: String = "",
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL
)
