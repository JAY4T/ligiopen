package com.jabulani.ligiopen.ui.inapp.fixtures

import com.jabulani.ligiopen.data.db.model.UserAccount
import com.jabulani.ligiopen.data.db.model.variables.userAccountDt
import com.jabulani.ligiopen.data.network.model.club.ClubDetails
import com.jabulani.ligiopen.data.network.model.match.fixture.FixtureData
import com.jabulani.ligiopen.ui.inapp.clubs.LoadingStatus
import java.time.LocalDate

data class FixturesUiData(
    val userAccount: UserAccount = userAccountDt,
    val fixtures: List<FixtureData> = emptyList(),
    val clubIds: List<Int> = emptyList(),
    val clubs: List<ClubDetails> = emptyList(),
    val selectedClubs: List<ClubDetails> = emptyList(),
    val selectedDate: LocalDate? = null,
    val matchDateTimes: List<String> = emptyList(),
    val matchDateTimesFetched: Boolean = false,
    val unauthorized: Boolean = false,
    val clubIsNull: Boolean = true,
    val singleClubMode: Boolean = false,
    val singleClubId: Int? = null,
    val loadingStatus: LoadingStatus = LoadingStatus.LOADING
)
