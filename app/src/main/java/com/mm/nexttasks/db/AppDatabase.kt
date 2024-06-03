package com.mm.nexttasks.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mm.nexttasks.db.dao.*
import com.mm.nexttasks.db.entities.*
import com.mm.nexttasks.db.views.TaskDetails

@Database(entities = [Task::class, TaskList::class, Category::class, Priority::class], views=[TaskDetails::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun taskListDao(): TaskListDao
    abstract fun categoryDao(): CategoryDao
    abstract fun priorityDao(): PriorityDao
}