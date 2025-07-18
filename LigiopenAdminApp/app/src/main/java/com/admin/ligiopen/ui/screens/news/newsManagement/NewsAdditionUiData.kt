package com.admin.ligiopen.ui.screens.news.newsManagement

import android.net.Uri
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.club.ClubData
import com.admin.ligiopen.data.network.models.news.NewsDto
import com.admin.ligiopen.data.network.models.news.singleNews
import com.admin.ligiopen.data.room.db.userAccountDt
import com.admin.ligiopen.data.room.models.UserAccount

data class NewsAdditionUiData(
    val userAccount: UserAccount = userAccountDt,
    val selectedClubIds: List<Int> = emptyList(),
    val publishedNews: NewsDto = singleNews,
    val clubs: List<ClubData> = emptyList(),
    val coverPhoto: Uri? = null,
    val title: String = "",
    val subtitle: String = "",
    val buttonEnabled: Boolean = false,
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL
)
