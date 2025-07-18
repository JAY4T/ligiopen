package com.admin.ligiopen.ui.screens.match.fixtures

import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.club.ClubData
import com.admin.ligiopen.data.network.models.match.location.MatchLocationData
import com.admin.ligiopen.data.room.db.userAccountDt
import com.admin.ligiopen.data.room.models.UserAccount
import java.time.LocalDate
import java.time.LocalTime

data class FixtureCreationUiData(
    val userAccount: UserAccount = userAccountDt,
    val clubSearchText: String = "",
    val awaySearchText: String = "",
    val home: ClubData? = null,
    val away: ClubData? = null,
    val matchDate: LocalDate? = null,
    val matchTime: LocalTime? = null,
    val clubs: List<ClubData> = emptyList(),
    val locationSearchText: String = "",
    val selectedLocation: MatchLocationData? = null,
    val locations: List<MatchLocationData> = emptyList(),
    val errorMsg: String = "",
    val buttonEnabled: Boolean = false,
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL
)
