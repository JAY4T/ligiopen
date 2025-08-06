package com.admin.ligiopen.ui.screens.news

import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.news.NewsDto
import com.admin.ligiopen.data.network.models.news.singleNews
import com.admin.ligiopen.data.room.db.userAccountDt
import com.admin.ligiopen.data.room.models.UserAccount

data class NewsDetailsScreenUiData(
    val userAccount: UserAccount = userAccountDt,
    val news: NewsDto = singleNews,
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL
)
