package com.jabulani.ligiopen

import android.app.Application
import com.jabulani.ligiopen.data.container.AppContainer
import com.jabulani.ligiopen.data.container.AppContainerImpl

class Ligiopen: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}