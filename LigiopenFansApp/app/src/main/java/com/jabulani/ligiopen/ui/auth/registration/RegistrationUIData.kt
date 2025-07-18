package com.jabulani.ligiopen.ui.auth.registration

data class RegistrationUIData(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isButtonEnabled: Boolean = false,
    val resultMessage: String = "",
    val registrationStatus: RegistrationStatus = RegistrationStatus.INITIAL
)
