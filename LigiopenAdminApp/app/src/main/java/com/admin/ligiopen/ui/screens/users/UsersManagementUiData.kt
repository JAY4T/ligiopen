package com.admin.ligiopen.ui.screens.users

import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.user.UserDto
import com.admin.ligiopen.data.room.db.userAccountDt
import com.admin.ligiopen.data.room.models.UserAccount

data class UsersManagementUiData(
    val userAccount: UserAccount = userAccountDt,
    val users: List<UserDto> = emptyList(),
    val currentTab: UserRole = UserRole.SUPER_ADMIN,
    val loadingStatus: LoadingStatus = LoadingStatus.LOADING
)
