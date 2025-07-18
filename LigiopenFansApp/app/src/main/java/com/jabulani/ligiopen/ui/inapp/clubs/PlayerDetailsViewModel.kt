package com.jabulani.ligiopen.ui.inapp.clubs

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jabulani.ligiopen.data.db.DBRepository
import com.jabulani.ligiopen.data.db.model.UserAccount
import com.jabulani.ligiopen.data.network.ApiRepository
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
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _uiState = MutableStateFlow(PlayerDetailsScreenUiData())
    val uiState: StateFlow<PlayerDetailsScreenUiData> = _uiState.asStateFlow()

    private val playerId: String? = savedStateHandle[PlayerDetailsScreenDestination.playerId]

    private fun getPlayer() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }
        viewModelScope.launch {
            try {
                val response = apiRepository.getPlayerById(
                    token = uiState.value.userAccount.token,
                    id = playerId!!.toInt()
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            playerDetails = response.body()?.data!!,
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }

                    getClub(response.body()?.data?.clubId!!)
                }

            } catch (e: Exception) {

            }
        }
    }

    private fun getClub(playerId: Int) {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }
        viewModelScope.launch {
            try {
                val response = apiRepository.getClubById(
                    token = uiState.value.userAccount.token,
                    id = playerId
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            clubDetails = response.body()?.data!!,
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                }

            } catch (e: Exception) {

            }
        }
    }

    fun getPlayerScreenUiData() {
        viewModelScope.launch {
            while (uiState.value.userAccount.id == 0) {
                delay(1000)
            }
            getPlayer()
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepository.getUsers().collect { users ->
                    _uiState.update {
                        it.copy(
                            userAccount = if(users.isNotEmpty()) users[0] else UserAccount(0, "", "", "", "", "", "")
                        )
                    }
                }
            }
        }
    }

    init {
        getUser()
        getPlayerScreenUiData()
    }
}