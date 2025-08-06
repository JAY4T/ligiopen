package com.admin.ligiopen.ui.screens.match.clubs

import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.club.ClubData
import com.admin.ligiopen.data.room.db.userAccountDt
import com.admin.ligiopen.data.room.models.UserAccount

data class ClubsUiData(
    val userAccount: UserAccount = userAccountDt,
    val clubs: List<ClubData> = emptyList(),
    val unauthorized: Boolean = false,
    val currentTab: ClubStatus = ClubStatus.APPROVED,
    val loadingStatus: LoadingStatus = LoadingStatus.LOADING
)
