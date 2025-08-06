package com.admin.ligiopen.ui.screens.match.clubs

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.club.ClubUpdateRequest
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
import java.time.LocalDate

class ClubUpdateViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(ClubUpdateUiData())
    val uiState: StateFlow<ClubUpdateUiData> = _uiState.asStateFlow()

    private val clubId: String? = savedStateHandle[ClubUpdateScreenDestination.clubId]

    fun changeName(name: String) {
        _uiState.update {
            it.copy(
                name = name,
            )
        }
        enableUpdateButton()
    }

    fun changeAbbreviation(abbreviation: String) {
        val club = uiState.value.clubData
        _uiState.update {
            it.copy(
                abbreviation = abbreviation,
            )
        }
        enableUpdateButton()
    }

    fun changeDescription(desc: String) {
        val club = uiState.value.clubData
        _uiState.update {
            it.copy(
                description = desc,
            )
        }
        enableUpdateButton()
    }

    fun changeStartDate(startDate: LocalDate) {
        _uiState.update {
            it.copy(
                startedOn = startDate
            )
        }
        enableUpdateButton()
    }

    fun changeCountry(country: String) {
        _uiState.update {
            it.copy(
                country = country,
            )
        }
        enableUpdateButton()
    }

    fun changeCounty(county: String) {
        _uiState.update {
            it.copy(
                county = county,
            )
        }
        enableUpdateButton()
    }

    fun changeTown(town: String) {
        _uiState.update {
            it.copy(
                town = town,
            )
        }
        enableUpdateButton()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getClub() {
        viewModelScope.launch {
            try {
                val response = apiRepository.getClub(
                    token = uiState.value.userAccount.token,
                    clubId = clubId!!.toInt()
                )

                if(response.isSuccessful) {
                    val club = response.body()?.data!!
                    _uiState.update {
                        it.copy(
                            clubData = club,
                            name = club.name,
                            abbreviation = club.clubAbbreviation ?: "",
                            description = club.description,
                            startedOn = LocalDate.parse(club.startedOn),
                            country = club.country,
                            county = club.county ?: "",
                            town = club.town ?: ""
                        )
                    }
                } else {
                    val errorString = response.errorBody()?.string()
                    Log.e("getClub", "ResponseErr: $errorString")
                }
            } catch (e: Exception) {
                Log.e("getClub", "Exception: $e")
            }
        }
    }

    fun updateClubDetails() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }

        viewModelScope.launch {
            try {
                val clubUpdateRequest = ClubUpdateRequest(
                    clubId = uiState.value.clubData.clubId,
                    divisionId = null,
                    homeId = null,
                    name = uiState.value.name,
                    clubAbbreviation = uiState.value.abbreviation,
                    description = uiState.value.description,
                    startedOn = uiState.value.startedOn!!.toString(),
                    country = uiState.value.country,
                    county = uiState.value.county,
                    town = uiState.value.town
                )

                val response = apiRepository.updateClubDetails(
                    token = uiState.value.userAccount.token,
                    clubUpdateRequest = clubUpdateRequest
                )

                if(response.isSuccessful) {
                    getClub()
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
                    Log.e("clubUpdate", "ResponseErr: $$response")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }
                Log.e("clubUpdate", "ResponseEsc: $e")

            }
        }
    }

    fun getInitialData() {
        viewModelScope.launch {
            while (uiState.value.userAccount.id == 0) {
                delay(1000)
            }
            getClub()
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

    private fun enableUpdateButton() {
        _uiState.update {
            it.copy(
                buttonEnabled = uiState.value.clubData.name != uiState.value.name ||
                uiState.value.clubData.clubAbbreviation != uiState.value.abbreviation ||
                uiState.value.clubData.description != uiState.value.description ||
                uiState.value.clubData.startedOn != uiState.value.startedOn!!.toString() ||
                uiState.value.clubData.country != uiState.value.country ||
                uiState.value.clubData.county != uiState.value.county ||
                uiState.value.clubData.town != uiState.value.town
            )
        }
    }

    init {
        loadUserData()
        getInitialData()
    }
}