package com.admin.ligiopen

import android.app.Application

class Ligiopen: Application() {
    lateinit var container: com.admin.ligiopen.container.AppContainer

    override fun onCreate() {
        super.onCreate()
        container = com.admin.ligiopen.container.AppContainerImpl(this)
    }
}