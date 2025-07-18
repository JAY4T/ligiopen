package com.jabulani.ligiopen.ui.inapp.news

import com.jabulani.ligiopen.data.db.model.UserAccount
import com.jabulani.ligiopen.data.db.model.variables.userAccountDt
import com.jabulani.ligiopen.data.network.model.news.NewsDto
import com.jabulani.ligiopen.data.network.model.news.singleNews
import com.jabulani.ligiopen.ui.inapp.clubs.LoadingStatus

data class NewsDetailsScreenUiData(
    val userAccount: UserAccount = userAccountDt,
    val news: NewsDto = singleNews,
    val loadingStatus: LoadingStatus = LoadingStatus.LOADING
)