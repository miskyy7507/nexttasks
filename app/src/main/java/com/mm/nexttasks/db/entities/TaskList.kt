package com.mm.nexttasks.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskList (
    @PrimaryKey(autoGenerate = true) val taskListId: Long,
    val name: String,
) {
    override fun toString() = this.name
}