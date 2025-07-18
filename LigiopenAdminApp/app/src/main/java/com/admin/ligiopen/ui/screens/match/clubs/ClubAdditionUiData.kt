package com.admin.ligiopen.ui.screens.match.clubs

import android.net.Uri
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.room.db.userAccountDt
import com.admin.ligiopen.data.room.models.UserAccount
import java.time.LocalDate

data class ClubAdditionUiData(
    val userAccount: UserAccount = userAccountDt,
    val clubName: String = "",
    val clubDescription: String = "",
    val country: String = "Kenya",
    val county: String = "Nairobi",
    val town: String = "",
    val startedOn: LocalDate? = null,
    val clubLogo: Uri? = null,
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL
)
