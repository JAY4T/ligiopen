package com.admin.ligiopen.ui.screens.users

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.user.ClubAdminSetRequestBody
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

class ClubSearchViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(ClubSearchUiData())
    val uiState: StateFlow<ClubSearchUiData> = _uiState.asStateFlow()

    val userId: String? = savedStateHandle[ClubSearchScreenDestination.userId]

    private fun getUser() {
        viewModelScope.launch {
            try {
                val response = apiRepository.getUser(
                    token = uiState.value.userAccount.token,
                    userId = userId!!.toInt()
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            user = response.body()?.data!!
                        )
                    }

                } else {
                    Log.e("getUser", "ResponseErr: $response")
                }

            } catch (e: Exception) {
                Log.e("getUser", "Exception: $e")

            }
        }
    }

    fun setAdmin() {
        _uiState.update {
            it.copy(
                setStatus = SetStatus.LOADING
            )
        }

        viewModelScope.launch {

            val clubAdminSetRequestBody = ClubAdminSetRequestBody(
                userId = userId!!.toInt(),
                teamId = uiState.value.selectedClubId
            )

            try {
                val response = apiRepository.setTeamAdmin(
                    token = uiState.value.userAccount.token,
                    clubAdminSetRequestBody = clubAdminSetRequestBody
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            setStatus = SetStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            setStatus = SetStatus.FAILURE
                        )
                    }

                    Log.e("setAdmin", "Type: team-admin, ResponseErr: $response")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        setStatus = SetStatus.FAILURE
                    )
                }

                Log.e("setAdmin", "Type: team-admin, Exception: $e")
            }
        }
    }

    fun changeClubName(name: String) {
        _uiState.update {
            it.copy(
                clubName = name
            )
        }

        if(name.isNotEmpty()) {
            getClubs()
        }
    }

    fun selectClub(id: Int, clubName: String) {
        _uiState.update {
            it.copy(
                selectedClubId = id,
                selectedClubName = clubName
            )
        }
    }

    private fun getClubs() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }

        viewModelScope.launch {
            try {
                val response = apiRepository.getClubs(
                    token = uiState.value.userAccount.token,
                    clubName = uiState.value.clubName,
                    divisionId = null,
                    userId = uiState.value.userAccount.id,
                    favorite = null,
                    status = null,
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            clubs = response.body()?.data!!,
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.FAIL
                        )
                    }

                    Log.e("getUsers", "ResponseErr: $response")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }

                Log.e("getUsers", "Exception: $e")
            }
        }
    }

    fun resetStatus() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.INITIAL,
                setStatus = SetStatus.INITIAL
            )
        }
    }

    fun getInitialData() {
        viewModelScope.launch {
            while (uiState.value.userAccount.id == 0) {
                delay(1000)
            }
            getUser()
            getClubs()
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