package com.admin.ligiopen.ui.screens.users

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.user.AdminSetRequestBody
import com.admin.ligiopen.data.network.repository.ApiRepository
import com.admin.ligiopen.data.room.db.userAccountDt
import com.admin.ligiopen.data.room.repository.DBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserSearchViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(UserSearchUiData())
    val uiState: StateFlow<UserSearchUiData> = _uiState.asStateFlow()



    private val adminType: String? = savedStateHandle[UserSearchScreenDestination.adminType]

    fun setAdmin() {
        _uiState.update {
            it.copy(
                setStatus = SetStatus.LOADING
            )
        }

        viewModelScope.launch {

            val adminSetRequestBody = AdminSetRequestBody(
                userId = uiState.value.selectedUserId
            )

            try {
                val response = if(adminType == "super-admin") apiRepository.setSuperAdmin(
                    token = uiState.value.userAccount.token,
                    adminSetRequestBody = adminSetRequestBody
                ) else apiRepository.setContentAdmin(
                    token = uiState.value.userAccount.token,
                    adminSetRequestBody = adminSetRequestBody
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            setStatus = SetStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            setStatus = SetStatus.FAILURE
                        )
                    }

                    Log.e("setAdmin", "Type: $adminType, ResponseErr: $response")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        setStatus = SetStatus.FAILURE
                    )
                }

                Log.e("setAdmin", "Type: $adminType, Exception: $e")
            }
        }
    }

    fun changeUsername(name: String) {
        _uiState.update {
            it.copy(
                username = name
            )
        }

        if(name.isNotEmpty()) {
            getUsers()
        }
    }

    fun selectUser(id: Int, username: String) {
        _uiState.update {
            it.copy(
                selectedUserId = id,
                selectedUsername = username
            )
        }
    }

    private fun getUsers() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }

        viewModelScope.launch {

            try {
                val response = apiRepository.getUsers(
                    token = uiState.value.userAccount.token,
                    username = uiState.value.username.takeIf { it.isNotEmpty() },
                    role = null
                )

//                val type = response.body()?.data!!.

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            users = response.body()?.data!!.filter { user -> user.role.lowercase().replace("_", "-") != adminType },
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.FAIL
                        )
                    }

                    Log.e("getUsers", "ResponseErr: $response")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }

                Log.e("getUsers", "Exception: $e")
            }
        }
    }

    fun resetStatus() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.INITIAL,
                setStatus = SetStatus.INITIAL
            )
        }
    }

    fun getInitialData() {
        viewModelScope.launch {
            while (uiState.value.userAccount.id == 0) {
                delay(1000)
            }

            getUsers()
        }
    }

    private fun loadUserData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepository.getUsers().collect {users ->
                    _uiState.update {
                        it.copy(
                            userAccount = userAccountDt.takeIf { users.isEmpty() } ?: users[0]
                        )
                    }
                }
            }
        }
    }

    init {
        _uiState.update {
            it.copy(
                adminType = adminType
            )
        }
        loadUserData()
    }
}