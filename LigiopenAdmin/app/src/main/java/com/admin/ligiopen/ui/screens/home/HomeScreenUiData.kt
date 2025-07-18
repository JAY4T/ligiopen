package com.admin.ligiopen.ui.screens.home

import com.admin.ligiopen.data.room.db.userAccountDt
import com.admin.ligiopen.data.room.models.UserAccount

data class HomeScreenUiData(
    val userAccount: UserAccount = userAccountDt,
    val logoutStatus: LogoutStatus = LogoutStatus.INITIAL
)
