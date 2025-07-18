package com.admin.ligiopen.ui.screens.match.fixtures

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

class FixturesViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(FixturesUiData())
    val uiState: StateFlow<FixturesUiData> = _uiState.asStateFlow()

    private fun getMatchFixtures() {
//        _uiState.update {
//            it.copy(
//                loadingStatus = LoadingStatus.LOADING
//            )
//        }

        viewModelScope.launch {
            try {
               val response = apiRepository.getMatchFixtures(
                   token = uiState.value.userAccount.token,
                   status = null
               )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            fixtures = response.body()?.data!!,
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                    Log.d("getMatchFixtures", "success: ${response.body()}")
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

                    Log.e("getMatchFixtures", "response: $response")
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }

                Log.e("getMatchFixtures", "e: $e")

            }
        }
    }

    fun getInitialData() {
        viewModelScope.launch {
            while (uiState.value.userAccount.id == 0) {
                delay(1000)
            }
            getMatchFixtures()
        }
    }

    fun resetStatus() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
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
        getInitialData()
    }
}