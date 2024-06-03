package com.mm.nexttasks.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskList (
    @PrimaryKey(autoGenerate = true) val taskListId: Int,
    val name: String,
)