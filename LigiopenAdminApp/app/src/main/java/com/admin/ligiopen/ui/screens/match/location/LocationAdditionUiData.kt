package com.admin.ligiopen.ui.screens.match.location

import android.net.Uri
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.room.db.userAccountDt
import com.admin.ligiopen.data.room.models.UserAccount

data class LocationAdditionUiData(
    val userAccount: UserAccount = userAccountDt,
    val county: String = "Nairobi",
    val country: String = "Kenya",
    val town: String = "",
    val venueName: String = "",
    val photos: List<Uri> = emptyList(),
    val locationId: Int? = null,
    val buttonEnabled: Boolean = false,
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL
)
