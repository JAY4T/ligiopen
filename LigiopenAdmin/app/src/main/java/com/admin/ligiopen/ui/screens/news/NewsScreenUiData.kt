package com.admin.ligiopen.ui.screens.news

import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.news.NewsDto
import com.admin.ligiopen.data.room.db.userAccountDt
import com.admin.ligiopen.data.room.models.UserAccount

data class NewsScreenUiData(
    val news: List<NewsDto> = emptyList(),
    val loadingStatus: LoadingStatus = LoadingStatus.LOADING,
    val unauthorized: Boolean = false,
    val currentTab: NewsStatus = NewsStatus.APPROVED,
    val userAccount: UserAccount = userAccountDt
)
