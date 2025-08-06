package com.jabulani.ligiopen.ui.inapp.clubs

import com.jabulani.ligiopen.data.db.model.UserAccount
import com.jabulani.ligiopen.data.db.model.variables.userAccountDt
import com.jabulani.ligiopen.data.network.model.club.ClubDetails
import com.jabulani.ligiopen.data.network.model.club.ClubDivisionDt

data class ClubsScreenUiData(
    val userAccount: UserAccount = userAccountDt,
    val clubs: List<ClubDetails> = emptyList(),
    val divisions: List<ClubDivisionDt> = emptyList(),
    val clubSearchText: String = "",
    val selectedDivision: ClubDivisionDt? = null,
    val bookmarkClubId: Int? = null,
    val favorite: Boolean = false,
    val loadingStatus: LoadingStatus = LoadingStatus.LOADING,
)
