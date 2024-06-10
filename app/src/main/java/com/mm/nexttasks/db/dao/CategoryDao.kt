package com.mm.nexttasks.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mm.nexttasks.db.entities.Category

@Dao
interface CategoryDao {
    @Query("SELECT * FROM Category")
    fun getAll(): List<Category>

    @Query("SELECT * FROM Category WHERE categoryId = :categoryId")
    fun getFromId(categoryId: Long): Category

    @Insert
    fun insert(category: Category): Long

    @Delete
    fun delete(category: Category)
}