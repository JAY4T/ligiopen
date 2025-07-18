package com.admin.ligiopen.ui.screens.player

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.club.ClubUpdateRequest
import com.admin.ligiopen.data.network.models.player.PlayerUpdateRequest
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

class PlayerDetailsViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _uiState = MutableStateFlow(PlayerDetailsUiData())
    val uiState: StateFlow<PlayerDetailsUiData> = _uiState.asStateFlow()

    private val playerId: String? = savedStateHandle[PlayerDetailsScreenDestination.playerId]

    fun changeUsername(name: String) {
        _uiState.update {
            it.copy(
                username = name
            )
        }
    }

    fun changeAge(age: String) {
        _uiState.update {
            it.copy(
                age = age
            )
        }
    }

    fun changeHeight(height: String) {
        _uiState.update {
            it.copy(
                height = height
            )
        }
    }

    fun changeWeight(weight: String) {
        _uiState.update {
            it.copy(
                weight = weight
            )
        }
    }

    fun changeNumber(number: String) {
        _uiState.update {
            it.copy(
                number = number
            )
        }
    }

    fun changePlayerPosition(position: String) {
        _uiState.update {
            it.copy(
                playerPosition = position
            )
        }
    }

    fun changeCountry(country: String) {
        _uiState.update {
            it.copy(
                country = country
            )
        }
    }

    fun changeCounty(county: String) {
        _uiState.update {
            it.copy(
                county = county
            )
        }
    }

    fun changeTown(town: String) {
        _uiState.update {
            it.copy(
                town = town
            )
        }
    }

    fun updatePlayerDetails() {

        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }

        viewModelScope.launch {
            try {
                val playerUpdateRequest = PlayerUpdateRequest(
                    playerId = playerId!!.toInt(),
                    username = uiState.value.username,
                    age = uiState.value.age.toInt(),
                    height = uiState.value.height.toDouble(),
                    weight = uiState.value.weight.toDouble(),
                    number = uiState.value.number.toInt(),
                    playerPosition = uiState.value.playerPosition,
                    country = uiState.value.country,
                    county = uiState.value.county,
                    town = uiState.value.town
                )

                val response = apiRepository.updatePlayerDetails(
                    token = uiState.value.userAccount.token,
                    playerUpdateRequest = playerUpdateRequest
                )

                if(response.isSuccessful) {
                    getPlayer()
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                } else {
                    val errorString = response.errorBody()?.string()
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.FAIL
                        )
                    }
                    Log.e("playerUpdate", "ResponseErr: $$response")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }
                Log.e("playerUpdate", "ResponseEsc: $e")

            }
        }
    }

    private fun getPlayer() {
        viewModelScope.launch {
            try {
                val response = apiRepository.getPlayer(
                    token = uiState.value.userAccount.token,
                    playerId = playerId!!.toInt()
                )

                if(response.isSuccessful) {
                    val player = response.body()?.data!!
                    _uiState.update {
                        it.copy(
                            player = player,
                            username = player.username,
                            age = player.age.toString(),
                            height = player.height.toString(),
                            weight = player.weight.toString(),
                            number = player.number.toString(),
                            playerPosition = player.playerPosition.name,
                            country = player.country,
                            county = player.county,
                            town = player.town
                        )
                    }
                } else {
                    val errorString = response.errorBody()?.string()
                    Log.e("getPlayer", "ResponseErr: $errorString")
                }
            } catch (e: Exception) {
                Log.e("getPlayer", "Exception: $e")
            }
        }
    }

    fun getInitialData() {
        viewModelScope.launch {
            while (uiState.value.userAccount.id == 0) {
                delay(1000)
            }
            getPlayer()
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