package com.admin.ligiopen.ui.screens.match.location

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

class LocationsViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(LocationUiData())
    val uiState: StateFlow<LocationUiData> = _uiState.asStateFlow()

    fun getMatchLocations() {
        _uiState.update {
            it.copy(
//                loadingStatus = LoadingStatus.LOADING
            )
        }

        viewModelScope.launch {
            try {
               val response = apiRepository.getMatchLocations(
                   token = uiState.value.userAccount.token,
                   venueName = null,
                   locationName = null
               )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            matchLocations = response.body()?.data!!,
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                } else {
                    if(response.code() == 401) {
                        _uiState.update {
                            it.copy(
                                unaAuthorized = true
                            )
                        }
                    }
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.FAIL
                        )
                    }

                    Log.e("loadMatchLocations", "response: $response")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }

                Log.e("loadMatchLocations", "e: $e")
            }
        }
    }

    fun getInitialData() {
        viewModelScope.launch {
            while (uiState.value.userAccount.id == 0) {
                delay(1000)
            }
            getMatchLocations()
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