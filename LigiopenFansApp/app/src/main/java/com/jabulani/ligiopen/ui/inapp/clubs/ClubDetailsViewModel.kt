package com.jabulani.ligiopen.ui.inapp.clubs

import android.util.Log
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

class ClubDetailsViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(ClubDetailsScreenUiData())
    val uiState: StateFlow<ClubDetailsScreenUiData> = _uiState.asStateFlow()

    private val clubId: String? = savedStateHandle[ClubDetailsScreenDestination.clubId]

    private fun getClub() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }
        viewModelScope.launch {
            try {
                val response = apiRepository.getClubById(
                    token = uiState.value.userAccount.token,
                    id = clubId!!.toInt()
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            clubDetails = response.body()?.data!!,

                        )
                    }
                    getNews()
                }

            } catch (e: Exception) {

            }
        }
    }

    private fun getNews() {

        viewModelScope.launch {
            try {
                val response = apiRepository.getAllNews(
                    token = uiState.value.userAccount.token,
                    clubId = uiState.value.clubDetails.clubId
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            clubNews = response.body()?.data!!,
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                } else {
                    Log.e("fetchNews", "response: $response")
                }

            } catch (e: Exception) {
                Log.e("fetchNews", "e: $e")

            }
        }
    }

    fun getClubScreenUiData() {
        viewModelScope.launch {
            while (uiState.value.userAccount.id == 0) {
                delay(1000)
            }
            getClub()
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
        getClubScreenUiData()
    }
}