package com.jabulani.ligiopen.ui.inapp.fixtures.fixtureDetails

import com.jabulani.ligiopen.ui.inapp.fixtures.fixtureDetails.lineup.PlayerInLineup
import com.jabulani.ligiopen.data.db.model.UserAccount
import com.jabulani.ligiopen.data.db.model.variables.userAccountDt
import com.jabulani.ligiopen.data.network.model.club.ClubDetails
import com.jabulani.ligiopen.data.network.model.club.club
import com.jabulani.ligiopen.data.network.model.location.MatchLocationData
import com.jabulani.ligiopen.data.network.model.location.matchLocation
import com.jabulani.ligiopen.data.network.model.match.commentary.MatchCommentaryData
import com.jabulani.ligiopen.data.network.model.match.fixture.FixtureData
import com.jabulani.ligiopen.data.network.model.match.fixture.fixture
import com.jabulani.ligiopen.data.network.model.player.PlayerDetails
import com.jabulani.ligiopen.data.network.model.player.player
import com.jabulani.ligiopen.ui.inapp.clubs.LoadingStatus

data class HighlightsScreenUiData(
    val userAccount: UserAccount = userAccountDt,
    val postMatchId: String? = null,
    val fixtureId: String? = null,
    val awayClubScore: Int = 0,
    val homeClubScore: Int = 0,
    val awayClubPlayers: List<PlayerDetails> = emptyList(),
    val homeClubPlayers: List<PlayerDetails> = emptyList(),
    val playersInLineup: List<PlayerInLineup> = emptyList(),
    val matchFixtureData: FixtureData = fixture,
    val matchLocationData: MatchLocationData = matchLocation,
    val commentaries: List<MatchCommentaryData> = emptyList(),
    val mainPlayer: PlayerDetails = player,
    val secondaryPlayer: PlayerDetails = player,
    val mainPlayerClub: ClubDetails = club,
    val secondaryPlayerClub: ClubDetails = club,
    val loadingStatus: LoadingStatus = LoadingStatus.LOADING,
)
