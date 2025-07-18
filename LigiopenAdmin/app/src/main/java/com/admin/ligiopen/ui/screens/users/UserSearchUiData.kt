package com.admin.ligiopen.ui.screens.users

import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.user.UserDto
import com.admin.ligiopen.data.room.db.userAccountDt
import com.admin.ligiopen.data.room.models.UserAccount

data class UserSearchUiData(
    val userAccount: UserAccount = userAccountDt,
    val users: List<UserDto> = emptyList(),
    val username: String = "",
    val selectedUserId: Int = 0,
    val selectedUsername: String = "",
    val adminType: String? = null,
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL,
    val setStatus: SetStatus = SetStatus.INITIAL
)