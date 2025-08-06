package com.jabulani.ligiopen.ui.auth.login

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jabulani.ligiopen.data.db.DBRepository
import com.jabulani.ligiopen.data.db.model.UserAccount
import com.jabulani.ligiopen.data.network.ApiRepository
import com.jabulani.ligiopen.data.network.model.user.UserLoginRequestBody
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(LoginUIData())
    val uiState: StateFlow<LoginUIData> = _uiState.asStateFlow()

    private val email: String? = savedStateHandle[LoginScreenDestination.email]
    private val password: String? = savedStateHandle[LoginScreenDestination.password]

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

    fun loginUser() {
        val loginRequestBody = UserLoginRequestBody(
            email = uiState.value.email,
            password = uiState.value.password
        )
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    loginStatus = LoginStatus.LOADING
                )
            }
            try {
                val response = apiRepository.login(
                    userLoginRequestBody = loginRequestBody
                )

                if(response.isSuccessful) {
                    val userAccount = UserAccount(
                        id = response.body()?.data?.user?.id!!,
                        username = response.body()?.data?.user?.username!!,
                        email = response.body()?.data?.user?.email!!,
                        password = uiState.value.password,
                        role = response.body()?.data?.user?.role!!,
                        createdAt = response.body()?.data?.user?.createdAt!!,
                        token = response.body()?.data?.token!!
                    )
                    dbRepository.insertUser(userAccount)

                    var users = dbRepository.getUsers().first()

                    while(users.isEmpty()) {
                        delay(1000)
                        users = dbRepository.getUsers().first()
                    }

                    _uiState.update {
                        it.copy(
                            loginMessage = "Login successful",
                            loginStatus = LoginStatus.SUCCESS
                        )
                    }
                    Log.d("loginResult", "Login successful!")
                } else {
                    if(response.code() == 401) {
                        _uiState.update {
                            it.copy(
                                loginMessage = "Invalid credentials",
                                loginStatus = LoginStatus.FAIL
                            )
                        }
                        Log.e("loginResult", "Invalid credentials")
                    } else {
                        _uiState.update {
                            it.copy(
                                loginMessage = "Login failed",
                                loginStatus = LoginStatus.FAIL
                            )
                        }
                        Log.e("loginResult", response.toString())
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loginMessage = "Login failed",
                        loginStatus = LoginStatus.FAIL
                    )
                }
                Log.e("loginResult", e.toString())
            }
        }
    }

    fun resetStatus() {
        _uiState.update {
            it.copy(
                loginStatus = LoginStatus.INITIAL
            )
        }
    }

    private fun enableButton() {
        _uiState.update {
            it.copy(
                isButtonEnabled = uiState.value.email.isNotEmpty() && uiState.value.password.isNotEmpty() && uiState.value.loginStatus != LoginStatus.LOADING
            )
        }
    }

    init {
        _uiState.update {
            it.copy(
                email = email ?: "",
                password = password ?: ""
            )
        }
        enableButton()
    }
}