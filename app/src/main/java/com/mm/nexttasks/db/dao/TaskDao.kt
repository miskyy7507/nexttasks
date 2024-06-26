package com.mm.nexttasks.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mm.nexttasks.db.entities.Task
import com.mm.nexttasks.db.views.TaskDetails

@Dao
interface TaskDao {
    @Query("SELECT * FROM TaskDetails")
    fun getAll(): List<TaskDetails>

    @Query("SELECT * FROM TaskDetails WHERE taskListName LIKE :taskListName")
    fun getTasksFromList(taskListName: String): List<TaskDetails>

    @Query("SELECT * FROM Task WHERE taskId = :taskId")
    fun getTaskFromId(taskId: Long): Task

    @Query("SELECT * FROM TaskDetails WHERE term >= :day AND term < :day + 86400000")
    fun getTaskFromDay(day: Long): List<TaskDetails>

    @Insert
    fun insert(task: Task): Long

    @Update
    fun editTask(task: Task)

    @Query("UPDATE Task SET isDone = :isDone WHERE taskId = :taskId")
    fun updateDoneState(taskId: Long, isDone: Boolean)

    @Delete
    fun delete(task: Task)
}