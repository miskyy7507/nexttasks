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
            database!!.taskListDao().insert(TaskList(0, getText(R.string.default_task_list_main).toString()))
        }
    }

    companion object {
        var database: AppDatabase? = null
            private set
    }
}