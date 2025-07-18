package com.jabulani.ligiopen.ui.inapp.home

import com.jabulani.ligiopen.data.db.model.UserAccount

data class HomeScreenUIData(
    val userAccount: UserAccount = UserAccount(1, "", "", "", "", "", "")
)
