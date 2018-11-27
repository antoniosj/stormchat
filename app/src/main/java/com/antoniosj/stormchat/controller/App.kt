package com.antoniosj.stormchat.controller

import android.app.Application
import com.antoniosj.stormchat.utilities.SharedPrefs

class App: Application() {

    companion object {
        lateinit var prefs: SharedPrefs
    }

    override fun onCreate() {

        prefs = SharedPrefs(applicationContext)
        super.onCreate()
    }
}