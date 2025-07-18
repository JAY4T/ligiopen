package com.admin.ligiopen.container

import com.admin.ligiopen.data.network.repository.ApiRepository
import com.admin.ligiopen.data.room.repository.DBRepository

interface AppContainer {
    val apiRepository: ApiRepository
    val dbRepository: DBRepository
}