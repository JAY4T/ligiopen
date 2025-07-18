package com.admin.ligiopen.ui.screens.users

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin.ligiopen.data.network.enums.LoadingStatus
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

class UsersManagementViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(UsersManagementUiData())
    val uiState: StateFlow<UsersManagementUiData> = _uiState.asStateFlow()

    fun switchTab(tab: UserRole) {
        _uiState.update {
            it.copy(
                currentTab = tab
            )
        }
        getUsers()
    }

    private fun getUsers() {
        _uiState.update {
            it.copy(
//                loadingStatus = LoadingStatus.LOADING
            )
        }

        viewModelScope.launch {
            try {
                val response = apiRepository.getUsers(
                    token = uiState.value.userAccount.token,
                    username = null,
                    role = uiState.value.currentTab.name
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            users = response.body()?.data!!,
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

    fun getInitialData() {
        viewModelScope.launch {
            while (uiState.value.userAccount.id == 0) {
                delay(1000)
            }
            getUsers()
        }
    }

    fun resetStatus() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.INITIAL
            )
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
        loadUserData()
    }
}