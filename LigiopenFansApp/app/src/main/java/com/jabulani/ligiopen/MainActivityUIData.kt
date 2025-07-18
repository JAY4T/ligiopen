package com.jabulani.ligiopen

import com.jabulani.ligiopen.data.db.model.UserAccount

data class MainActivityUIData(
    val userAccount: UserAccount = UserAccount(1, "", "", "", "", "", "")
)
