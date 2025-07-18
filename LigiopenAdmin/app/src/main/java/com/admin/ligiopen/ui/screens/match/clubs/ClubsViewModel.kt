package com.admin.ligiopen.ui.screens.match.clubs

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

class ClubsViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(ClubsUiData())
    val uiState: StateFlow<ClubsUiData> = _uiState.asStateFlow()

    fun changeTab(tab: ClubStatus) {
        _uiState.update {
            it.copy(
                currentTab = tab
            )
        }
        getClubs()
    }

    private fun getClubs() {
        Log.d("getClub", "Tab: ${uiState.value.currentTab.name}")
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING,
                clubs = emptyList()
            )
        }
        viewModelScope.launch {
            try {
               val response = apiRepository.getClubs(
                   token = uiState.value.userAccount.token,
                   clubName = null,
                   divisionId = null,
                   userId = uiState.value.userAccount.id,
                   favorite = null,
                   status = uiState.value.currentTab.name
               )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.SUCCESS,
                            clubs = response.body()?.data!!
                        )
                    }
                } else {
                    if(response.code() == 401) {
                        _uiState.update {
                            it.copy(
                                unauthorized = true
                            )
                        }
                    }
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.FAIL
                        )
                    }
                    Log.e("getClubs", "response: $response")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }
                Log.e("getClubs", "e: $e")
            }
        }
    }

    fun getInitialData() {
        viewModelScope.launch {
            while (uiState.value.userAccount.id == 0) {
                delay(1000)
            }
            getClubs()
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
                loadingStatus = LoadingStatus.INITIAL,
            )
        }
    }

    init {
        loadUserData()
        getInitialData()
    }
}