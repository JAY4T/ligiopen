package com.jabulani.ligiopen.ui.inapp.news

import com.jabulani.ligiopen.data.db.model.UserAccount
import com.jabulani.ligiopen.data.db.model.variables.userAccountDt
import com.jabulani.ligiopen.data.network.model.news.NewsDto
import com.jabulani.ligiopen.ui.inapp.clubs.LoadingStatus

data class NewsScreenUiData(
    val news: List<NewsDto> = emptyList(),
    val loadingStatus: LoadingStatus = LoadingStatus.LOADING,
    val unauthorized: Boolean = false,
    val clubId: Int? = null,
    val userAccount: UserAccount = userAccountDt
)