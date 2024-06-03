package com.mm.nexttasks.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Task (
    @PrimaryKey(autoGenerate = true) val taskId: Int,
    val title: String,
    val taskListId: Int,
    val priorityId: Int?,
    val categoryId: Int?,
    val isDone: Boolean,
    val cardColor: Int?,
    val term: Date?,
)