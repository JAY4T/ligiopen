package com.admin.ligiopen.ui.screens.match.location

import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.match.location.MatchLocationData
import com.admin.ligiopen.data.room.db.userAccountDt
import com.admin.ligiopen.data.room.models.UserAccount

data class LocationUiData(
    val userAccount: UserAccount = userAccountDt,
    val matchLocations: List<MatchLocationData> = emptyList(),
    val unaAuthorized: Boolean = false,
    val loadingStatus: LoadingStatus = LoadingStatus.LOADING
)
