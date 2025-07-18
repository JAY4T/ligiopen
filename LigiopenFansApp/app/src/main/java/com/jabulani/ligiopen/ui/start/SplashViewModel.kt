package com.jabulani.ligiopen.ui.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jabulani.ligiopen.data.db.DBRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel(
    private val dbRepository: DBRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(SplashUIData())
    val uiState: StateFlow<SplashUIData> = _uiState.asStateFlow()

    private fun getUsers() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    users = dbRepository.getUsers().first()
                )
            }
        }
    }

    fun changeLoadingStatus() {
        _uiState.update {
            it.copy(
                isLoading = false
            )
        }
    }

    init {
        getUsers()
    }
}