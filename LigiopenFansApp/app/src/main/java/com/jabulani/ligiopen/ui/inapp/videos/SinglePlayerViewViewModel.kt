package com.jabulani.ligiopen.ui.inapp.videos

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SinglePlayerViewViewModel(
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _uiState = MutableStateFlow(SinglePlayerViewUiData())
    val uiState: StateFlow<SinglePlayerViewUiData> = _uiState.asStateFlow()

    private val videoId: String? = savedStateHandle[SinglePlayerViewScreenDestination.videoId]
    private val videoTitle: String? = savedStateHandle[SinglePlayerViewScreenDestination.videoTitle]
    private val videoDate: String? = savedStateHandle[SinglePlayerViewScreenDestination.videoDate]

    init {
        _uiState.update {
            it.copy(
                videoId = videoId ?: "",
                videoTitle = videoTitle ?: "",
                videoDate = videoDate ?: ""
            )
        }
    }
}