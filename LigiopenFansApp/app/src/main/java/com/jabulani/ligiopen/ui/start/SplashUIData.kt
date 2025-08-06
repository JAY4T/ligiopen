package com.jabulani.ligiopen.ui.start

import com.jabulani.ligiopen.data.db.model.UserAccount

data class SplashUIData(
    val users: List<UserAccount> = emptyList(),
    val isLoading: Boolean = true,
)
