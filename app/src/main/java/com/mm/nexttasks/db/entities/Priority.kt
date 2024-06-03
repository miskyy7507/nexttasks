package com.mm.nexttasks.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Priority (
    @PrimaryKey(autoGenerate = true) val priorityId: Int,
    val name: String,
) {
    override fun toString() = this.name
}