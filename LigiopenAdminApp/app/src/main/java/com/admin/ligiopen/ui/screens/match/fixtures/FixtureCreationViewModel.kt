package com.admin.ligiopen.ui.screens.match.fixtures

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.club.ClubData
import com.admin.ligiopen.data.network.models.match.fixture.FixtureCreationRequest
import com.admin.ligiopen.data.network.models.match.fixture.MatchStatus
import com.admin.ligiopen.data.network.models.match.location.MatchLocationData
import com.admin.ligiopen.data.network.repository.ApiRepository
import com.admin.ligiopen.data.room.db.userAccountDt
import com.admin.ligiopen.data.room.repository.DBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class FixtureCreationViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(FixtureCreationUiData())
    val uiState: StateFlow<FixtureCreationUiData> = _uiState.asStateFlow()

    fun changeClubSearchText(text: String) {
        _uiState.update {
            it.copy(
                clubSearchText = text
            )
        }
        if(text.isNotEmpty()) {
            getClubs()
        } else {
            _uiState.update {
                it.copy(
                    clubs = emptyList()
                )
            }
        }
    }

    fun changeHomeClub(home: ClubData) {
        _uiState.update {
            it.copy(
                home = home
            )
        }
        enableButton()
        _uiState.update {
            it.copy(
                clubSearchText = "",
                clubs = emptyList()
            )
        }
    }

    fun removeHomeClub() {
        _uiState.update {
            it.copy(
                home = null
            )
        }
        enableButton()
    }


    fun changeAwayClub(away: ClubData) {
        _uiState.update {
            it.copy(
                away = away
            )
        }
        enableButton()
        _uiState.update {
            it.copy(
                clubSearchText = "",
                clubs = emptyList()
            )
        }
    }

    fun removeAwayClub() {
        _uiState.update {
            it.copy(
                away = null
            )
        }
        enableButton()
    }

    fun changeSearchLocationText(text: String) {
        _uiState.update {
            it.copy(
                locationSearchText = text
            )
        }
        if(text.isNotEmpty()) {
            getMatchLocations()
        } else {
            _uiState.update {
                it.copy(
                    locations = emptyList()
                )
            }
        }

    }

    fun selectMatchLocation(location: MatchLocationData) {
        _uiState.update {
            it.copy(
                selectedLocation = location
            )
        }
        enableButton()
        _uiState.update {
            it.copy(
                locationSearchText = "",
                locations = emptyList()
            )
        }

        Log.d("selectedLocation", uiState.value.selectedLocation.toString())
    }

    fun removeSelectedLocation() {
        _uiState.update {
            it.copy(
                selectedLocation = null
            )
        }
        enableButton()
    }

    fun selectMatchDate(date: LocalDate) {
        _uiState.update {
            it.copy(
                matchDate = date
            )
        }
        enableButton()
    }

    fun selectMatchTime(time: LocalTime) {
        _uiState.update {
            it.copy(
                matchTime = time
            )
        }
        enableButton()
    }

    private fun getClubs() {
        viewModelScope.launch {
            try {
                val response = apiRepository.getClubs(
                    token = uiState.value.userAccount.token,
                    clubName = uiState.value.clubSearchText,
                    divisionId = null,
                    userId = uiState.value.userAccount.id,
                    favorite = null,
                    status =null
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            clubs = response.body()?.data!!.filter { club -> club.clubStatus == "APPROVED" }.filter { club -> club.clubId != uiState.value.home?.clubId && club.clubId != uiState.value.away?.clubId }
                        )
                    }
                } else {
                    if(response.code() == 401) {
                        _uiState.update {
                            it.copy(
//                                unauthorized = true
                            )
                        }
                    }
                    _uiState.update {
                        it.copy(
//                            loadingStatus = LoadingStatus.FAIL
                        )
                    }
                    Log.e("getClubs", "response: $response")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
//                        loadingStatus = LoadingStatus.FAIL
                    )
                }
                Log.e("getClubs", "e: $e")
            }
        }
    }

    private fun getMatchLocations() {
//        _uiState.update {
//            it.copy(
//                loadingStatus = LoadingStatus.LOADING
//            )
//        }

        viewModelScope.launch {
            try {
                val response = apiRepository.getMatchLocations(
                    token = uiState.value.userAccount.token,
                    venueName = uiState.value.locationSearchText,
                    locationName = null
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            locations = response.body()?.data!!,
                        )
                    }
                } else {
                    if(response.code() == 401) {
                        _uiState.update {
                            it.copy(
//                                unaAuthorized = true
                            )
                        }
                    }
                    _uiState.update {
                        it.copy(
//                            loadingStatus = LoadingStatus.FAIL
                        )
                    }

                    Log.e("loadMatchLocations", "response: $response")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
//                        loadingStatus = LoadingStatus.FAIL
                    )
                }

                Log.e("loadMatchLocations", "e: $e")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createMatchFixture() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }

        enableButton()

        viewModelScope.launch {
            try {

                val matchDateTime = LocalDateTime.of(uiState.value.matchDate!!, uiState.value.matchTime!!)
                val fixtureCreationRequest = FixtureCreationRequest(
                    homeClub = uiState.value.home!!.clubId,
                    awayClub = uiState.value.away!!.clubId,
                    matchDateTime = matchDateTime.toString(),
                    matchStatus = MatchStatus.PENDING,
                    locationId = uiState.value.selectedLocation!!.locationId
               )

                val response = apiRepository.createMatchFixture(
                    token = uiState.value.userAccount.token,
                    fixtureCreation = fixtureCreationRequest
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                    enableButton()
                } else {
                    _uiState.update {
                        it.copy(
                            errorMsg = response.toString(),
                            loadingStatus = LoadingStatus.FAIL
                        )
                    }
                    enableButton()

                    Log.e("createFixture", "ResponseErr: $response")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMsg = e.toString(),
                        loadingStatus = LoadingStatus.FAIL
                    )
                }

                enableButton()

                Log.e("createFixture", "Exception: $e")

            }
        }

    }

    fun resetStatus() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
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

    private fun enableButton() {
        _uiState.update {
            it.copy(
                buttonEnabled = uiState.value.home != null &&
                uiState.value.away != null &&
                uiState.value.matchDate != null &&
                uiState.value.matchTime != null &&
                uiState.value.selectedLocation != null &&
                uiState.value.loadingStatus != LoadingStatus.LOADING
            )
        }
    }

    init {
        loadUserData()
    }
}