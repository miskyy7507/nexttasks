package com.mm.nexttasks

import android.app.Application
import androidx.room.Room
import com.mm.nexttasks.db.AppDatabase
import com.mm.nexttasks.db.entities.TaskList

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app-db-main"
        ).allowMainThreadQueries().build()

        // if no task lists, create a new, 'main' one
        if (database!!.taskListDao().getAll().isEmpty()) {
            database!!.taskListDao().insert(TaskList(0, "Główna lista zadań"))
            database!!.taskListDao().insert(TaskList(0, "Poboczna lista zadań"))
            database!!.taskListDao().insert(TaskList(0, "Lista zadań nr 3"))
        }
    }

    companion object {
        var database: AppDatabase? = null
            private set
    }
}