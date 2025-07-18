package com.jabulani.ligiopen.ui.inapp.fixtures

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jabulani.ligiopen.data.db.DBRepository
import com.jabulani.ligiopen.data.db.model.variables.userAccountDt
import com.jabulani.ligiopen.data.network.ApiRepository
import com.jabulani.ligiopen.data.network.model.club.ClubDetails
import com.jabulani.ligiopen.data.network.model.match.fixture.FixtureData
import com.jabulani.ligiopen.ui.inapp.clubs.LoadingStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime

class FixturesViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(FixturesUiData())
    val uiState: StateFlow<FixturesUiData> = _uiState.asStateFlow()

    fun setSingleClubMode(singleClubMode: Boolean, clubId: Int?) {
        _uiState.update {
            it.copy(
                singleClubMode = singleClubMode,
                singleClubId = clubId
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setClubIsNull(clubIsNull: Boolean) {
        _uiState.update {
            it.copy(
                clubIsNull = clubIsNull
            )
        }
        getInitialData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateClubId(clubId: Int) {
        val clubIds = uiState.value.clubIds.toMutableList()
        if(clubIds.contains(clubId)) {
            clubIds.remove(clubId)
        } else {
            clubIds.add(clubId)
        }
        _uiState.update {
            it.copy(
                clubIds = clubIds
            )
        }
        getMatchFixtures()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun selectDate(date: LocalDate?) {
        _uiState.update {
            it.copy(
                selectedDate = date
            )
        }
        getMatchFixtures()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun selectClubs(club: ClubDetails) {
        val clubs = uiState.value.selectedClubs.toMutableList()
        clubs.add(club)
        _uiState.update {
            it.copy(
                selectedClubs = clubs
            )
        }
        getMatchFixtures()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deselectClub(club: ClubDetails) {
        val clubs = uiState.value.selectedClubs.toMutableList()
        clubs.remove(club)
        _uiState.update {
            it.copy(
                selectedClubs = clubs
            )
        }
        getMatchFixtures()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun resetClubs() {
        _uiState.update {
            it.copy(
                selectedClubs = emptyList()
            )
        }
        getMatchFixtures()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getMatchFixtures() {
//        _uiState.update {
//            it.copy(
//                loadingStatus = LoadingStatus.LOADING
//            )
//        }

        viewModelScope.launch {
            try {
               val response = apiRepository.getMatchFixtures(
                   token = uiState.value.userAccount.token,
                   status = null,
                   clubIds = uiState.value.selectedClubs.map { it.clubId },
                   matchDateTime = if(uiState.value.selectedDate != null) uiState.value.selectedDate.toString() else null,
               )

                val awayClubIds = response.body()?.data!!.map { it.awayClub.clubId }
                val homeClubIds = response.body()?.data!!.map { it.homeClub.clubId }

                val totalClubIds = awayClubIds + homeClubIds

                val fixtureClubIds = totalClubIds.distinct()

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            fixtures = if(!uiState.value.clubIsNull && uiState.value.singleClubMode && !fixtureClubIds.contains(uiState.value.singleClubId)) emptyList() else response.body()?.data!!,
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                    if(!uiState.value.matchDateTimesFetched) {
                        updateMatchDateTimes(response.body()?.data!!)
                    }
                    Log.d("getMatchFixtures", "success: ${response.body()}")
                } else {
                    if(response.code() == 401) {
                        _uiState.update {
                            it.copy(
                                unauthorized = true
                            )
                        }
                    }

                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.FAIL
                        )
                    }

                    Log.e("getMatchFixtures", "response: $response")
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }

                Log.e("getMatchFixtures", "e: $e")

            }
        }
    }

    private fun updateMatchDateTimes(fixtures: List<FixtureData>) {
        val matchDateTimes = mutableListOf<String>()
        fixtures.forEach { fixture ->
            matchDateTimes.add(fixture.matchDateTime)
        }
        val clubs = mutableListOf<ClubDetails>()
        for(fixture in fixtures) {
            clubs.add(fixture.homeClub)
            clubs.add(fixture.awayClub)
        }
        val uniqueClubs = clubs.distinctBy { it.clubId }
        _uiState.update {
            it.copy(
                matchDateTimes = matchDateTimes,
                clubs = uniqueClubs,
                matchDateTimesFetched = true
            )
        }
        Log.d("matchDateTimes", "matchDateTimes: ${uiState.value.matchDateTimes}")

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getInitialData() {
        viewModelScope.launch {
            while (uiState.value.userAccount.id == 0) {
                delay(1000)
            }
            getMatchFixtures()
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

    init {
        loadUserData()
    }
}