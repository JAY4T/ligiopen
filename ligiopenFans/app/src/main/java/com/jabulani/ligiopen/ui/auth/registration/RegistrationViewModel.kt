package com.jabulani.ligiopen.ui.auth.registration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jabulani.ligiopen.data.db.DBRepository
import com.jabulani.ligiopen.data.network.ApiRepository
import com.jabulani.ligiopen.data.network.model.user.UserRegistrationRequestBody
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(RegistrationUIData())
    val uiState: StateFlow<RegistrationUIData> = _uiState.asStateFlow()


    fun updateUsername(username: String) {
        _uiState.update {
            it.copy(
                username = username
            )
        }
        enableButton()
    }

    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(
                email = email
            )
        }
        enableButton()
    }

    fun updatePassword(password: String) {
        _uiState.update {
            it.copy(
                password = password
            )
        }
        enableButton()
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _uiState.update {
            it.copy(
                confirmPassword = confirmPassword
            )
        }
        enableButton()
    }

    fun registerUser() {
        val userRegistrationRequestBody = UserRegistrationRequestBody(
            username = uiState.value.username,
            email = uiState.value.email,
            role = "USER",
            password = uiState.value.password,
        )
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    registrationStatus = RegistrationStatus.LOADING
                )
            }
            try {
                val response = apiRepository.createUserAccount(
                    userRegistrationRequestBody = userRegistrationRequestBody
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            registrationStatus = RegistrationStatus.SUCCESS,
                            resultMessage = "Your account has been created successfully"
                        )
                    }
                    Log.d("registrationResult", "SUCCESS!")

                } else {
                    _uiState.update {
                        it.copy(
                            registrationStatus = RegistrationStatus.FAIL,
                            resultMessage = response.body().toString()
                        )
                    }
                    Log.e("registrationResult", response.toString())
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        registrationStatus = RegistrationStatus.FAIL,
                        resultMessage = e.toString()
                    )
                }
                Log.e("registrationResult", e.toString())
            }
        }
    }

    fun resetStatus() {
        _uiState.update {
            it.copy(
                registrationStatus = RegistrationStatus.INITIAL
            )
        }
    }

    private fun enableButton() {
        _uiState.update {
            it.copy(
                isButtonEnabled = uiState.value.username.isNotEmpty() && uiState.value.email.isNotEmpty() && uiState.value.password.isNotEmpty() && uiState.value.confirmPassword.isNotEmpty() && uiState.value.registrationStatus != RegistrationStatus.LOADING
            )
        }
    }
}