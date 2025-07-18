package com.admin.ligiopen.ui.screens.users

import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.club.ClubData
import com.admin.ligiopen.data.network.models.user.UserDto
import com.admin.ligiopen.data.network.models.user.emptyUser
import com.admin.ligiopen.data.room.db.userAccountDt
import com.admin.ligiopen.data.room.models.UserAccount

data class ClubSearchUiData(
    val userAccount: UserAccount = userAccountDt,
    val user: UserDto = emptyUser,
    val selectedClubId: Int = 0,
    val selectedClubName: String = "",
    val clubName: String = "",
    val clubs: List<ClubData> = emptyList(),
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL,
    val setStatus: SetStatus = SetStatus.INITIAL
)
