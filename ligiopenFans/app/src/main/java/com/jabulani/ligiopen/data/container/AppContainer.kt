package com.jabulani.ligiopen.data.container

import com.jabulani.ligiopen.data.db.DBRepository
import com.jabulani.ligiopen.data.network.ApiRepository

interface AppContainer {
    val apiRepository: ApiRepository
    val dbRepository: DBRepository
}