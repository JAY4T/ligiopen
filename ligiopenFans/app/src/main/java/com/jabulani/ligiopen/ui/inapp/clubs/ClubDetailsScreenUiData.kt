package com.jabulani.ligiopen.ui.inapp.clubs

import com.jabulani.ligiopen.data.db.model.UserAccount
import com.jabulani.ligiopen.data.db.model.variables.userAccountDt
import com.jabulani.ligiopen.data.network.model.club.ClubDetails
import com.jabulani.ligiopen.data.network.model.club.club
import com.jabulani.ligiopen.data.network.model.club.emptyClub
import com.jabulani.ligiopen.data.network.model.news.NewsDto

data class ClubDetailsScreenUiData(
    val userAccount: UserAccount = userAccountDt,
    val clubDetails: ClubDetails = emptyClub,
    val clubNews: List<NewsDto> = emptyList(),
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL
)
