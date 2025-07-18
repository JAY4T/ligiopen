package com.admin.ligiopen.ui.screens.news.newsManagement

import android.net.Uri
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.room.db.userAccountDt
import com.admin.ligiopen.data.room.models.UserAccount

data class NewsItemAdditionUiData(
    val userAccount: UserAccount = userAccountDt,
    val photo: Uri? = null,
    val title: String = "",
    val subtitle: String = "",
    val paragraph: String = "",
    val buttonEnabled: Boolean = false,
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL
)
