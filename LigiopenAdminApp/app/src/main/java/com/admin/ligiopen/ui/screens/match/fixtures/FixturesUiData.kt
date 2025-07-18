package com.admin.ligiopen.ui.screens.match.fixtures

import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.match.fixture.FixtureData
import com.admin.ligiopen.data.room.db.userAccountDt
import com.admin.ligiopen.data.room.models.UserAccount

data class FixturesUiData(
    val userAccount: UserAccount = userAccountDt,
    val fixtures: List<FixtureData> = emptyList(),
    val unauthorized: Boolean = false,
    val loadingStatus: LoadingStatus = LoadingStatus.LOADING
)
