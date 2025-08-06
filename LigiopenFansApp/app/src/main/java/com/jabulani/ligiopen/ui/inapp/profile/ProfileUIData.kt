package com.jabulani.ligiopen.ui.inapp.profile

import com.jabulani.ligiopen.data.db.model.UserAccount

data class ProfileUIData(
    val userAccount: UserAccount = UserAccount(1, "", "", "", "", "", "")
)
