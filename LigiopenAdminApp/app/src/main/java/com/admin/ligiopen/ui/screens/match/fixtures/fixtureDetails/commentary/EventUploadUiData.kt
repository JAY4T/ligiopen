package com.admin.ligiopen.ui.screens.match.fixtures.fixtureDetails.commentary

import android.net.Uri
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.club.ClubData
import com.admin.ligiopen.data.network.models.club.club
import com.admin.ligiopen.data.network.models.match.events.MatchEventType
import com.admin.ligiopen.data.network.models.match.fixture.FixtureData
import com.admin.ligiopen.data.network.models.match.fixture.fixtures
import com.admin.ligiopen.data.network.models.player.PlayerData
import com.admin.ligiopen.data.room.db.userAccountDt
import com.admin.ligiopen.data.room.models.UserAccount

data class EventUploadUiData(
    val userAccount: UserAccount = userAccountDt,
    val minute: String = "",
    val selectedEventType: String? = null,
    val matchEventType: MatchEventType? = null,
    val title: String = "",
    val summary: String = "",
    val mainPlayerText: String = "",
    val secondaryPlayerText: String = "",
    val file: Uri? = null,
    val commentaryId: Int? = null,
    val homeClub: ClubData = club,
    val awayClub: ClubData = club,
    val mainPlayer: PlayerClubData? = PlayerClubData(),
    val secondaryPlayer: PlayerClubData? = PlayerClubData(),
    val fixture: FixtureData = fixtures[0],
    val selectedPlayerClubData: PlayerClubData = PlayerClubData(),
    val mainPlayerClubDataList: List<PlayerClubData> = emptyList(),
    val secondaryPlayerClubDataList: List<PlayerClubData> = emptyList(),
    val isYellowCard: Boolean = false,
    val isRedCard: Boolean = false,
    val penaltyScored: Boolean = false,
    val freeKickScored: Boolean = false,
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL
)
