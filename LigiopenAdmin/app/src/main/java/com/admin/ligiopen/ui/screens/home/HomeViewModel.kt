package com.admin.ligiopen.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin.ligiopen.data.room.db.userAccountDt
import com.admin.ligiopen.data.room.repository.DBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val dbRepository: DBRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenUiData())
    val uiState: StateFlow<HomeScreenUiData> = _uiState.asStateFlow()

    fun logout() {
        _uiState.update {
            it.copy(
                logoutStatus = LogoutStatus.LOADING
            )
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    dbRepository.deleteUsers()
                    _uiState.update {
                        it.copy(
                            logoutStatus = LogoutStatus.SUCCESS
                        )
                    }
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(
                            logoutStatus = LogoutStatus.FAILURE
                        )
                    }
                }
            }
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

    fun resetStatus() {
        _uiState.update {
            it.copy(
                logoutStatus = LogoutStatus.INITIAL,
            )
        }
    }

    init {
        loadUserData()
    }
}