package com.jabulani.ligiopen.ui.inapp.news

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jabulani.ligiopen.data.db.DBRepository
import com.jabulani.ligiopen.data.db.model.variables.userAccountDt
import com.jabulani.ligiopen.data.network.ApiRepository
import com.jabulani.ligiopen.ui.inapp.clubs.LoadingStatus
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

    private fun getNews() {
        Log.i("fetchNews", "newsId: $newsId")
//        _uiState.update {
//            it.copy(
//                loadingStatus = LoadingStatus.LOADING
//            )
//        }
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