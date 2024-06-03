package com.mm.nexttasks.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Task (
    @PrimaryKey(autoGenerate = true) val taskId: Long,
    val title: String,
    val taskListId: Long,
    val priorityId: Long?,
    val categoryId: Long?,
    val isDone: Boolean,
    val cardColor: Int?,
    val term: Date?,
)