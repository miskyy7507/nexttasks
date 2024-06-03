package com.mm.nexttasks.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category (
    @PrimaryKey(autoGenerate = true) val categoryId: Long,
    val name: String,
) {
    override fun toString() = this.name
}