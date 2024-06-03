package com.mm.nexttasks

import android.app.Application
import androidx.room.Room
import com.mm.nexttasks.db.AppDatabase

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app-db-main"
        ).allowMainThreadQueries().build()
    }

    companion object {
        var database: AppDatabase? = null
            private set
    }
}