package com.mm.nexttasks

import android.app.Application

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseProvider.getDatabase(this)
    }
}