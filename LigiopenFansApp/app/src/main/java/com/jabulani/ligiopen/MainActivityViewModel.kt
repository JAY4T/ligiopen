package com.jabulani.ligiopen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jabulani.ligiopen.data.db.DBRepository
import com.jabulani.ligiopen.data.db.model.UserAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivityViewModel(
    private val dbRepository: DBRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(value = MainActivityUIData())
    val uiState: StateFlow<MainActivityUIData> = _uiState.asStateFlow()

    private fun getUser() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepository.getUsers().collect { users ->
                    _uiState.update {
                        it.copy(
                            userAccount = if(users.isNotEmpty()) users[0] else UserAccount(1, "", "", "", "", "", "")
                        )
                    }
                }
            }
        }
    }

    fun switchTheme() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepository.updateUser(
                    uiState.value.userAccount.copy(
                        darkMode = !uiState.value.userAccount.darkMode
                    )
                )
            }
        }
    }


    init {
        getUser()
    }
}