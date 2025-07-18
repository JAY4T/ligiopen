package com.admin.ligiopen.ui.screens.match.fixtures.fixtureDetails

import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.club.ClubData
import com.admin.ligiopen.data.network.models.club.club
import com.admin.ligiopen.data.network.models.match.commentary.MatchCommentaryData
import com.admin.ligiopen.data.network.models.match.fixture.FixtureData
import com.admin.ligiopen.data.network.models.match.fixture.fixture
import com.admin.ligiopen.data.network.models.match.location.MatchLocationData
import com.admin.ligiopen.data.network.models.match.location.matchLocation
import com.admin.ligiopen.data.network.models.player.PlayerData
import com.admin.ligiopen.data.network.models.player.player
import com.admin.ligiopen.data.room.db.userAccountDt
import com.admin.ligiopen.data.room.models.UserAccount
import com.admin.ligiopen.ui.screens.match.fixtures.fixtureDetails.lineup.PlayerInLineup

data class HighlightsScreenUiData(
    val userAccount: UserAccount = userAccountDt,
    val postMatchId: String? = null,
    val fixtureId: String? = null,
    val awayClubScore: Int = 0,
    val homeClubScore: Int = 0,
    val awayClubPlayers: List<PlayerData> = emptyList(),
    val homeClubPlayers: List<PlayerData> = emptyList(),
    val playersInLineup: List<PlayerInLineup> = emptyList(),
    val matchFixtureData: FixtureData = fixture,
    val matchLocationData: MatchLocationData = matchLocation,
    val commentaries: List<MatchCommentaryData> = emptyList(),
    val mainPlayer: PlayerData = player,
    val secondaryPlayer: PlayerData = player,
    val mainPlayerClub: ClubData = club,
    val secondaryPlayerClub: ClubData = club,
    val loadingStatus: LoadingStatus = LoadingStatus.LOADING,
)
