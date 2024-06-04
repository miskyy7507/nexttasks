package com.mm.nexttasks.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mm.nexttasks.db.entities.Task
import com.mm.nexttasks.db.views.TaskDetails

@Dao
interface TaskDao {
    @Query("SELECT * FROM TaskDetails")
    fun getAll(): List<TaskDetails>

    @Query("SELECT * FROM TaskDetails WHERE taskListName LIKE :taskListName")
    fun getTasksFromList(taskListName: String): List<TaskDetails>

    @Insert
    fun insert(task: Task): Long

    @Delete
    fun delete(task: Task)
}