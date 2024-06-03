package com.mm.nexttasks.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mm.nexttasks.db.entities.Priority

@Dao
interface PriorityDao {
    @Query("SELECT * FROM Priority")
    fun getAll(): List<Priority>

    @Insert
    fun insert(priority: Priority)

    @Delete
    fun delete(priority: Priority)
}