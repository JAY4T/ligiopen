package com.admin.ligiopen.ui.screens.news

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.club.ChangeClubStatusRequestBody
import com.admin.ligiopen.data.network.models.news.NewsStatusUpdateRequestBody
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

class NewsDetailsViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(NewsDetailsScreenUiData())
    val uiState: StateFlow<NewsDetailsScreenUiData> = _uiState.asStateFlow()

    private val newsId: String? = savedStateHandle[NewsDetailsScreenDestination.newsId]

    fun changeNewsStatus(status: String) {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }
        viewModelScope.launch {
            try {
                val newsStatusUpdateRequestBody = NewsStatusUpdateRequestBody(
                    newsId = newsId!!.toInt(),
                    newsStatus = status
                )

                val response = apiRepository.changeNewsStatus(
                    token = uiState.value.userAccount.token,
                    newsStatusUpdateRequestBody = newsStatusUpdateRequestBody
                )

                if(response.isSuccessful) {
                    getNews()
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.FAIL
                        )
                    }
                    val errorString = response.errorBody()?.string()
                    Log.e("changeNewsStatus: ", "RESPONSE: $errorString")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }
                Log.e("changeNewsStatus: ", "EXCEPTION: $e")
            }
        }
    }

    private fun getNews() {
        viewModelScope.launch {
            try {
                val response = apiRepository.getSingleNews(
                    token = uiState.value.userAccount.token,
                    newsId = newsId!!.toInt()
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            news = response.body()?.data!!,
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

    fun getInitialData() {
        viewModelScope.launch {
            while (uiState.value.userAccount.id == 0) {
                delay(1000)
            }
            getNews()
        }
    }

    init {
        loadUserData()
        getInitialData()
    }
}