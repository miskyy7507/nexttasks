package com.mm.nexttasks

import android.content.Context
import androidx.room.Room
import com.mm.nexttasks.db.AppDatabase
import com.mm.nexttasks.db.entities.TaskList

object DatabaseProvider {
    @Volatile
    private var DB_INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return DB_INSTANCE ?: synchronized(this) {
            val dbInstance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "app-db-main"
            ).allowMainThreadQueries().build()

            // if no task lists, create a new, 'main' one
            if (dbInstance.taskListDao().getAll().isEmpty()) {
                dbInstance.taskListDao().insert(TaskList(0, context.getString(R.string.default_task_list_main)))
            }

            DB_INSTANCE = dbInstance
            dbInstance
        }
    }
}