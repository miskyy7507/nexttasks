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

    @Insert
    fun insert(task: Task)

    @Delete
    fun delete(task: Task)
}