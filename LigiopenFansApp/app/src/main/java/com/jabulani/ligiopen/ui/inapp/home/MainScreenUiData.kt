package com.jabulani.ligiopen.ui.inapp.home

import com.jabulani.ligiopen.data.db.model.UserAccount
import com.jabulani.ligiopen.data.db.model.variables.userAccountDt
import com.jabulani.ligiopen.data.network.model.match.fixture.FixtureData
import com.jabulani.ligiopen.data.network.model.news.NewsDto
import com.jabulani.ligiopen.ui.inapp.clubs.LoadingStatus

data class MainScreenUiData(
    val userAccount: UserAccount = userAccountDt,
    val fixtures: List<FixtureData> = emptyList(),
    val news: List<NewsDto> = emptyList(),
    val unauthorized: Boolean = false,
    val loadingStatus: LoadingStatus = LoadingStatus.LOADING
)
