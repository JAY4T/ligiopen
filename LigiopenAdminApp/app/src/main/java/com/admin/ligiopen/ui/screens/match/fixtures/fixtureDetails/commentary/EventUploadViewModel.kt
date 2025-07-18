package com.admin.ligiopen.ui.screens.match.fixtures.fixtureDetails.commentary

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.match.commentary.CommentaryCreationRequest
import com.admin.ligiopen.data.network.models.match.events.EventCreationRequest
import com.admin.ligiopen.data.network.models.match.events.MatchEventType
import com.admin.ligiopen.data.network.models.player.PlayerData
import com.admin.ligiopen.data.network.models.player.PlayerState
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.FileInputStream

class EventUploadViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _uiState = MutableStateFlow(EventUploadUiData())
    val uiState: StateFlow<EventUploadUiData> = _uiState.asStateFlow()

    private val matchFixtureId: String? = savedStateHandle[EventUploadScreenDestination.matchFixtureId]

    fun updateMinute(minute: String) {
        _uiState.update {
            it.copy(
                minute = minute
            )
        }
    }

    fun updateEventType(event: String?) {
        val matchEvent = when (event) {
            "Goal" -> MatchEventType.GOAL
            "Own goal" -> MatchEventType.OWN_GOAL
            "Substitution" -> MatchEventType.SUBSTITUTION
            "Foul" -> MatchEventType.FOUL
            "Yellow card" -> MatchEventType.YELLOW_CARD
            "Red card" -> MatchEventType.RED_CARD
            "Offside" -> MatchEventType.OFFSIDE
            "Corner kick" -> MatchEventType.CORNER_KICK
            "Free kick" -> MatchEventType.FREE_KICK
            "Penalty" -> MatchEventType.PENALTY
            "Penalty missed" -> MatchEventType.PENALTY_MISSED
            "Injury" -> MatchEventType.INJURY
            "Throw in" -> MatchEventType.THROW_IN
            "Goal kick" -> MatchEventType.GOAL_KICK
            "Kick off" -> MatchEventType.KICK_OFF
            "Half time" -> MatchEventType.HALF_TIME
            "Full time" -> MatchEventType.FULL_TIME
            else -> null // Handle unexpected values gracefully
        }

        _uiState.update {
            it.copy(
                selectedEventType = event,
                matchEventType = matchEvent,
                mainPlayerText = "",
                secondaryPlayerText = "",
                mainPlayer = null,
                secondaryPlayer = null,
                minute = "",
                isYellowCard = false,
                isRedCard = false,
                penaltyScored = false,
                freeKickScored = false
            )
        }
    }

    fun updateIsYellowCard() {
        _uiState.update {
            it.copy(
                isYellowCard = !uiState.value.isYellowCard
            )
        }
    }

    fun updateIsRedCard() {
        _uiState.update {
            it.copy(
                isRedCard = !uiState.value.isRedCard
            )
        }
    }

    fun updateIsFreeKickScored() {
        _uiState.update {
            it.copy(
                freeKickScored = !uiState.value.freeKickScored
            )
        }
    }

    fun updateIsPenaltyScored() {
        _uiState.update {
            it.copy(
                penaltyScored = !uiState.value.penaltyScored
            )
        }
    }

    fun updateTitle(title: String) {
        _uiState.update {
            it.copy(
                title = title
            )
        }
    }

    fun updateSummary(summary: String) {
        _uiState.update {
            it.copy(
                summary = summary
            )
        }
    }

    fun uploadFile(file: Uri?) {
        _uiState.update {
            it.copy(
                file = file
            )
        }
    }

    fun onSearchMainPlayer(player: String) {
        val players = uiState.value.homeClub.players + uiState.value.awayClub.players

        _uiState.update {
            it.copy(
                mainPlayerText = player,
                mainPlayerClubDataList = players.filter { playerData -> playerData.username.lowercase().contains(player.lowercase()) }.map { filteredPlayerData -> playerToPlayerClubData(filteredPlayerData) }
            )
        }
    }

    fun onSearchSecondaryPlayer(player: String) {
        val players = uiState.value.homeClub.players + uiState.value.awayClub.players

        _uiState.update {
            it.copy(
                secondaryPlayerText = player,
                secondaryPlayerClubDataList = players.filter { playerData -> playerData.username.lowercase().contains(player.lowercase()) }.map { filteredPlayerData -> playerToPlayerClubData(filteredPlayerData) }
            )
        }
    }

    fun onSelectMainPlayer(playerClubData: PlayerClubData) {
        _uiState.update {
            it.copy(
                mainPlayer = playerClubData,
                mainPlayerClubDataList = emptyList()
            )
        }
    }

    fun onSelectSecondaryPlayer(playerClubData: PlayerClubData) {
        _uiState.update {
            it.copy(
                secondaryPlayer = playerClubData,
                secondaryPlayerClubDataList = emptyList()
            )
        }
    }

    private fun getMatchFixture() {
        viewModelScope.launch {
            try {
                val response = apiRepository.getMatchFixture(
                    token = uiState.value.userAccount.token,
                    fixtureId = matchFixtureId!!.toInt()
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            fixture = response.body()!!.data,
                            homeClub = response.body()?.data!!.homeClub,
                            awayClub = response.body()?.data!!.awayClub
                        )
                    }
                }

            } catch (e: Exception) {

            }
        }
    }

    private fun getHomeClub(clubId: Int) {
        viewModelScope.launch {
            try {
               val response = apiRepository.getClub(
                   uiState.value.userAccount.token,
                   clubId = clubId
               )
               if(response.isSuccessful) {
                   _uiState.update {
                       it.copy(
                           homeClub = response.body()!!.data
                       )
                   }
               }
            } catch (e: Exception) {

            }
        }
    }

    private fun getAwayClub(clubId: Int) {
        viewModelScope.launch {
            try {
                val response = apiRepository.getClub(
                    uiState.value.userAccount.token,
                    clubId = clubId
                )
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            awayClub = response.body()!!.data
                        )
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    fun addEvent(context: Context) {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }

        viewModelScope.launch {
            try {
               val eventCreationRequest = when(uiState.value.matchEventType) {
                   MatchEventType.GOAL -> {
                       EventCreationRequest(
                           title = uiState.value.title,
                           summary = uiState.value.summary,
                           minute = uiState.value.minute,
                           matchEventType = uiState.value.matchEventType!!,
                           mainPlayerId = uiState.value.mainPlayer?.playerId,
                           secondaryPlayerId = uiState.value.secondaryPlayer?.playerId,
                           fouledPlayerId = null,
                           isYellowCard = null,
                           isRedCard = null,
                           isScored = null
                       )
                   }
                   MatchEventType.OWN_GOAL -> {
                       EventCreationRequest(
                           title = uiState.value.title,
                           summary = uiState.value.summary,
                           minute = uiState.value.minute,
                           matchEventType = uiState.value.matchEventType!!,
                           mainPlayerId = uiState.value.mainPlayer?.playerId,
                           secondaryPlayerId = null,
                           fouledPlayerId = null,
                           isYellowCard = null,
                           isRedCard = null,
                           isScored = null
                       )
                   }
                   MatchEventType.SUBSTITUTION -> {
                       EventCreationRequest(
                           title = uiState.value.title,
                           summary = uiState.value.summary,
                           minute = uiState.value.minute,
                           matchEventType = uiState.value.matchEventType!!,
                           mainPlayerId = uiState.value.mainPlayer?.playerId,
                           secondaryPlayerId = uiState.value.secondaryPlayer?.playerId,
                           fouledPlayerId = null,
                           isYellowCard = null,
                           isRedCard = null,
                           isScored = null
                       )
                   }
                   MatchEventType.FOUL -> {
                       EventCreationRequest(
                           title = uiState.value.title,
                           summary = uiState.value.summary,
                           minute = uiState.value.minute,
                           matchEventType = uiState.value.matchEventType!!,
                           mainPlayerId = uiState.value.mainPlayer?.playerId,
                           secondaryPlayerId = uiState.value.secondaryPlayer?.playerId,
                           fouledPlayerId = null,
                           isYellowCard = uiState.value.isYellowCard,
                           isRedCard = uiState.value.isRedCard,
                           isScored = null
                       )
                   }
                   MatchEventType.YELLOW_CARD -> {
                       EventCreationRequest(
                           title = uiState.value.title,
                           summary = uiState.value.summary,
                           minute = uiState.value.minute,
                           matchEventType = uiState.value.matchEventType!!,
                           mainPlayerId = uiState.value.mainPlayer?.playerId,
                           secondaryPlayerId = null,
                           fouledPlayerId = null,
                           isYellowCard = true,
                           isRedCard = null,
                           isScored = null
                       )
                   }
                   MatchEventType.RED_CARD -> {
                       EventCreationRequest(
                           title = uiState.value.title,
                           summary = uiState.value.summary,
                           minute = uiState.value.minute,
                           matchEventType = uiState.value.matchEventType!!,
                           mainPlayerId = uiState.value.mainPlayer?.playerId,
                           secondaryPlayerId = null,
                           fouledPlayerId = null,
                           isYellowCard = null,
                           isRedCard = true,
                           isScored = null
                       )
                   }
                   MatchEventType.OFFSIDE -> {
                       EventCreationRequest(
                           title = uiState.value.title,
                           summary = uiState.value.summary,
                           minute = uiState.value.minute,
                           matchEventType = uiState.value.matchEventType!!,
                           mainPlayerId = uiState.value.mainPlayer?.playerId,
                           secondaryPlayerId = null,
                           fouledPlayerId = null,
                           isYellowCard = null,
                           isRedCard = null,
                           isScored = null
                       )
                   }
                   MatchEventType.CORNER_KICK -> {
                       EventCreationRequest(
                           title = uiState.value.title,
                           summary = uiState.value.summary,
                           minute = uiState.value.minute,
                           matchEventType = uiState.value.matchEventType!!,
                           mainPlayerId = uiState.value.mainPlayer?.playerId,
                           secondaryPlayerId = null,
                           fouledPlayerId = null,
                           isYellowCard = null,
                           isRedCard = null,
                           isScored = null
                       )
                   }
                   MatchEventType.FREE_KICK -> {
                       EventCreationRequest(
                           title = uiState.value.title,
                           summary = uiState.value.summary,
                           minute = uiState.value.minute,
                           matchEventType = uiState.value.matchEventType!!,
                           mainPlayerId = uiState.value.mainPlayer?.playerId,
                           secondaryPlayerId = null,
                           fouledPlayerId = null,
                           isYellowCard = null,
                           isRedCard = null,
                           isScored = null
                       )
                   }
                   MatchEventType.PENALTY -> {
                       EventCreationRequest(
                           title = uiState.value.title,
                           summary = uiState.value.summary,
                           minute = uiState.value.minute,
                           matchEventType = uiState.value.matchEventType!!,
                           mainPlayerId = uiState.value.mainPlayer?.playerId,
                           secondaryPlayerId = null,
                           fouledPlayerId = null,
                           isYellowCard = null,
                           isRedCard = null,
                           isScored = uiState.value.penaltyScored
                       )
                   }
                   MatchEventType.PENALTY_MISSED -> {
                       EventCreationRequest(
                           title = uiState.value.title,
                           summary = uiState.value.summary,
                           minute = uiState.value.minute,
                           matchEventType = uiState.value.matchEventType!!,
                           mainPlayerId = uiState.value.mainPlayer?.playerId,
                           secondaryPlayerId = null,
                           fouledPlayerId = null,
                           isYellowCard = null,
                           isRedCard = null,
                           isScored = false
                       )
                   }
                   MatchEventType.INJURY -> {
                       EventCreationRequest(
                           title = uiState.value.title,
                           summary = uiState.value.summary,
                           minute = uiState.value.minute,
                           matchEventType = uiState.value.matchEventType!!,
                           mainPlayerId = uiState.value.mainPlayer?.playerId,
                           secondaryPlayerId = null,
                           fouledPlayerId = null,
                           isYellowCard = null,
                           isRedCard = null,
                           isScored = null
                       )
                   }
                   MatchEventType.THROW_IN -> {
                       EventCreationRequest(
                           title = uiState.value.title,
                           summary = uiState.value.summary,
                           minute = uiState.value.minute,
                           matchEventType = uiState.value.matchEventType!!,
                           mainPlayerId = uiState.value.mainPlayer?.playerId,
                           secondaryPlayerId = null,
                           fouledPlayerId = null,
                           isYellowCard = null,
                           isRedCard = null,
                           isScored = null
                       )
                   }
                   MatchEventType.GOAL_KICK -> {
                       EventCreationRequest(
                           title = uiState.value.title,
                           summary = uiState.value.summary,
                           minute = uiState.value.minute,
                           matchEventType = uiState.value.matchEventType!!,
                           mainPlayerId = uiState.value.mainPlayer?.playerId,
                           secondaryPlayerId = null,
                           fouledPlayerId = null,
                           isYellowCard = null,
                           isRedCard = null,
                           isScored = null
                       )
                   }
                   MatchEventType.KICK_OFF -> {
                       EventCreationRequest(
                           title = uiState.value.title,
                           summary = uiState.value.summary,
                           minute = uiState.value.minute,
                           matchEventType = uiState.value.matchEventType!!,
                           mainPlayerId = uiState.value.mainPlayer?.playerId,
                           secondaryPlayerId = null,
                           fouledPlayerId = null,
                           isYellowCard = null,
                           isRedCard = null,
                           isScored = null
                       )
                   }
                   MatchEventType.HALF_TIME -> {
                       EventCreationRequest(
                           title = uiState.value.title,
                           summary = uiState.value.summary,
                           minute = uiState.value.minute,
                           matchEventType = uiState.value.matchEventType!!,
                           mainPlayerId = null,
                           secondaryPlayerId = null,
                           fouledPlayerId = null,
                           isYellowCard = null,
                           isRedCard = null,
                           isScored = null
                       )
                   }
                   MatchEventType.FULL_TIME -> {
                       EventCreationRequest(
                           title = uiState.value.title,
                           summary = uiState.value.summary,
                           minute = uiState.value.minute,
                           matchEventType = uiState.value.matchEventType!!,
                           mainPlayerId = null,
                           secondaryPlayerId = null,
                           fouledPlayerId = null,
                           isYellowCard = null,
                           isRedCard = null,
                           isScored = null
                       )
                   }
                   null -> {
                       EventCreationRequest(
                           title = uiState.value.title,
                           summary = uiState.value.summary,
                           minute = uiState.value.minute,
                           matchEventType = uiState.value.matchEventType!!,
                           mainPlayerId = uiState.value.mainPlayer?.playerId,
                           secondaryPlayerId = uiState.value.secondaryPlayer?.playerId,
                           fouledPlayerId = null,
                           isYellowCard = null,
                           isRedCard = null,
                           isScored = null
                       )
                   }
               }

               val commentaryCreationRequest = CommentaryCreationRequest(
                   postMatchAnalysisId = uiState.value.fixture.postMatchAnalysisId,
                   minute = uiState.value.minute,
                   matchEvent = eventCreationRequest
               )

                val response = apiRepository.uploadMatchCommentary(
                    token = uiState.value.userAccount.token,
                    commentaryCreationRequest = commentaryCreationRequest
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            commentaryId = response.body()?.data?.matchCommentaryId,
                        )
                    }
                    if(uiState.value.file != null) {
                        uploadFile(context)
                    } else {
                        _uiState.update {
                            it.copy(
                                commentaryId = response.body()?.data?.matchCommentaryId,
                                loadingStatus = LoadingStatus.SUCCESS
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.FAIL
                        )
                    }
                    Log.e("uploadMatchEvent", "response: $response")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }

                Log.e("uploadMatchEvent", "e: $e")

            }
        }
    }

    private fun uploadFile(context: Context) {
        viewModelScope.launch {
            try {
                var files = mutableListOf<MultipartBody.Part?>()
                val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uiState.value.file!!, "r", null)
                parcelFileDescriptor?.let { pfd ->
                    val inputStream = FileInputStream(pfd.fileDescriptor)
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    val buffer = ByteArray(1024)
                    var length: Int
                    while(inputStream.read(buffer).also { length = it } != -1) {
                        byteArrayOutputStream.write(buffer, 0, length)
                    }
                    val byteArray = byteArrayOutputStream.toByteArray()

                    //Get the MIME type of the file

                    val mimeType = context.contentResolver.getType(uiState.value.file!!)
                    val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                    val requestFile = RequestBody.create(mimeType?.toMediaTypeOrNull(), byteArray)
                    val imagePart = MultipartBody.Part.createFormData("file", "upload.$extension", requestFile)
                    files.add(imagePart)
                }

                val response = apiRepository.uploadMatchCommentaryFiles(
                    token = uiState.value.userAccount.token,
                    commentaryId = uiState.value.commentaryId!!,
                    files = files
                )

                if(response.isSuccessful) {
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
                    Log.e("uploadMatchEvent", "response: $response")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }
                Log.e("uploadMatchEvent", "e: $e")
            }
        }
    }

    private fun getInitialData() {
        viewModelScope.launch {
            while (uiState.value.userAccount.id == 0) {
                delay(1000)
            }
            getMatchFixture()
        }
    }

    fun resetStatus() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.INITIAL
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



    private fun playerToPlayerClubData(playerData: PlayerData): PlayerClubData {
        val home = playerData.clubId == uiState.value.homeClub.clubId
        val playerClubData = PlayerClubData(
            playerId = playerData.playerId,
            clubId = playerData.clubId!!,
            name = playerData.username,
            home = home,
            bench = playerData.playerState == PlayerState.BENCH,
            clubLogo = if(home) uiState.value.homeClub.clubLogo.link else uiState.value.awayClub.clubLogo.link
        )
        return playerClubData
    }

    init {
        loadUserData()
        getInitialData()
    }

}