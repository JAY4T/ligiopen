package com.admin.ligiopen.ui.screens.start

import com.admin.ligiopen.data.room.models.UserAccount

data class SplashUIData(
    val users: List<UserAccount> = emptyList(),
    val isLoading: Boolean = true,
)
