package com.admin.ligiopen.ui.screens.auth

data class LoginUIData(
    val email: String = "",
    val password: String = "",
    val isButtonEnabled: Boolean = false,
    val loginMessage: String = "",
    val loginStatus: LoginStatus = LoginStatus.INITIAL,
)
