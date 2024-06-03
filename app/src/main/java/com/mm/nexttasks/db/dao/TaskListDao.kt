package com.mm.nexttasks.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mm.nexttasks.db.entities.TaskList

@Dao
interface TaskListDao {
    @Query("SELECT * FROM TaskList")
    fun getAll(): List<TaskList>

    @Insert
    fun insert(taskList: TaskList): Long

    @Delete
    fun delete(taskList: TaskList)
}