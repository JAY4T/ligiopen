package com.admin.ligiopen.ui.screens.match.clubs

import android.net.Uri
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.club.ClubData
import com.admin.ligiopen.data.network.models.club.emptyClub
import com.admin.ligiopen.data.room.db.userAccountDt
import com.admin.ligiopen.data.room.models.UserAccount

data class ClubDetailsUiData(
    val userAccount: UserAccount = userAccountDt,
    val club: ClubData = emptyClub,
    val newLogo: Uri? = null,
    val newPhoto: Uri? = null,
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL
)
