package com.admin.ligiopen.ui.screens.match.clubs

import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.club.ClubData
import com.admin.ligiopen.data.network.models.club.club
import com.admin.ligiopen.data.room.db.userAccountDt
import com.admin.ligiopen.data.room.models.UserAccount
import java.time.LocalDate

data class ClubUpdateUiData(
    val userAccount: UserAccount = userAccountDt,
    val clubData: ClubData = club,
    val name: String = "",
    val abbreviation: String = "",
    val description: String = "",
    val startedOn: LocalDate? = null,
    val country: String = "",
    val county: String = "",
    val town: String = "",
    val buttonEnabled: Boolean = false,
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL
)
