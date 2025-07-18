package com.jabulani.ligiopen.ui.inapp.fixtures.fixtureDetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jabulani.ligiopen.ui.inapp.fixtures.fixtureDetails.lineup.PlayerInLineup
import com.jabulani.ligiopen.data.db.DBRepository
import com.jabulani.ligiopen.data.db.model.variables.userAccountDt
import com.jabulani.ligiopen.data.network.ApiRepository
import com.jabulani.ligiopen.data.network.model.match.commentary.MatchCommentaryData
import com.jabulani.ligiopen.data.network.model.match.fixture.FixtureData
import com.jabulani.ligiopen.data.network.model.player.PlayerDetails
import com.jabulani.ligiopen.data.network.model.player.PlayerState
import com.jabulani.ligiopen.ui.inapp.clubs.LoadingStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HighlightsScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(HighlightsScreenUiData())
    val uiState: StateFlow<HighlightsScreenUiData> = _uiState.asStateFlow()

    private val postMatchId: String? = savedStateHandle[HighlightsScreenDestination.postMatchId]
    private val fixtureId: String? = savedStateHandle[HighlightsScreenDestination.fixtureId]
    private val locationId: String? = savedStateHandle[HighlightsScreenDestination.locationId]

    private fun getMatchCommentary() {
        viewModelScope.launch {
            try {
                val response = apiRepository.getFullMatchDetails(
                    token = uiState.value.userAccount.token,
                    postMatchAnalysisId = postMatchId!!.toInt()
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            commentaries = response.body()?.data?.minuteByMinuteCommentary!!,
                            awayClubScore = response.body()?.data?.awayClubScore ?: 0,
                            homeClubScore = response.body()?.data?.homeClubScore ?: 0,
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.FAIL
                        )
                    }
                    Log.e("matchHighlights", "response: $response")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }
                Log.e("matchHighlights", "e: $e")
            }
        }
    }

    private fun getMatchLocation() {
        viewModelScope.launch {
            try {
               val response = apiRepository.getMatchLocation(
                   token = uiState.value.userAccount.token,
                   locationId = locationId!!.toInt()
               )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            matchLocationData = response.body()?.data!!
                        )
                    }
                } else {
                    Log.e("matchLocation", "response: $response")
                }

            } catch (e: Exception) {
                Log.e("matchLocation", "e: $e")
            }
        }
    }

    private fun getMatchFixture() {
        viewModelScope.launch {
            try {
                val response = apiRepository.getMatchFixture(
                    token = uiState.value.userAccount.token,
                    fixtureId = fixtureId!!.toInt()
                )

                if(response.isSuccessful) {

                    _uiState.update {
                        it.copy(
                            matchFixtureData = response.body()?.data!!,
                            awayClubPlayers = response.body()?.data?.awayClub?.players!!,
                            homeClubPlayers = response.body()?.data?.homeClub?.players!!,
                        )
                    }
                } else {
                    Log.e("matchFixture", "response: $response")
                }

            } catch (e: Exception) {
                Log.e("matchFixture", "e: $e")
            }
        }
    }

    fun getMainPlayer(playerId: Int) {
        viewModelScope.launch {
            try {
               val response = apiRepository.getPlayer(
                   token = uiState.value.userAccount.token,
                   playerId = playerId
               )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            mainPlayer = response.body()?.data!!
                        )
                    }
                }

            } catch (e: Exception) {

            }
        }
    }

    fun getSecondaryPlayer(playerId: Int) {
        viewModelScope.launch {
            try {
                val response = apiRepository.getPlayer(
                    token = uiState.value.userAccount.token,
                    playerId = playerId
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            secondaryPlayer = response.body()?.data!!
                        )
                    }
                }

            } catch (e: Exception) {

            }
        }
    }

    fun getInitialData() {
        viewModelScope.launch {
            while (uiState.value.userAccount.id == 0) {
                delay(1000)
            }
            getMatchCommentary()
            getMatchLocation()
            getMatchFixture()
            loadPlayersLineup()
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
                            userAccount = userAccountDt.takeIf { users.isEmpty() } ?: users[0],
                            postMatchId = postMatchId,
                            fixtureId = fixtureId
                        )
                    }
                }
            }
        }
    }

    private fun loadPlayersLineup() {
        viewModelScope.launch {
            while (uiState.value.matchFixtureData.matchFixtureId == 0 || uiState.value.commentaries.isEmpty()) {
                delay(1000)
            }
            val allPlayers = uiState.value.awayClubPlayers + uiState.value.homeClubPlayers
            _uiState.update {
                it.copy(
                    playersInLineup = allPlayers.map { player -> playerToPlayerInLineup(player, uiState.value.matchFixtureData, uiState.value.commentaries) }
                )
            }
        }
    }

    private fun playerToPlayerInLineup(player: PlayerDetails, fixture: FixtureData, commentaries: List<MatchCommentaryData>): PlayerInLineup {
        return PlayerInLineup(
            position = player.playerPosition,
            name = player.username,
            team = if(player.clubId == fixture.awayClub.clubId) fixture.awayClub.name else fixture.homeClub.name,
            home = player.clubId == fixture.homeClub.clubId,
            number = player.number,
            substituted = commentaries.any { commentary ->
            commentary.substitutionEvent?.mainPlayerId == player.playerId ||
                    commentary.substitutionEvent?.subbedOutPlayerId == player.playerId },
            yellowCard = commentaries.any { commentary ->
                commentary.foulEvent?.mainPlayerId == player.playerId && commentary.foulEvent.isYellowCard
            },
            redCard = commentaries.any { commentary ->
                commentary.foulEvent?.mainPlayerId == player.playerId && commentary.foulEvent.isRedCard
            },
            scored = commentaries.any { commentary ->
                commentary.goalEvent?.mainPlayerId == player.playerId
            },
            bench = player.playerState == PlayerState.BENCH
        )
    }

    init {
        loadUserData()
        getInitialData()
    }
}