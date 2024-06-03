package com.mm.nexttasks.db.views

import androidx.room.DatabaseView
import java.util.Date

@DatabaseView("""
    SELECT Task.*, Category.name as categoryName, Priority.name as priorityName, TaskList.name as taskListName FROM Task
    LEFT JOIN Category ON Task.categoryId = Category.categoryId
    LEFT JOIN Priority ON Task.priorityId = Priority.priorityId
    INNER JOIN TaskList ON Task.taskListId = TaskList.taskListId;
""")
data class TaskDetails (
    val taskId: Int,
    val title: String,
    val taskListId: Int,
    val taskListName: String,
    val priorityId: Int?,
    val priorityName: String?,
    val categoryId: Int?,
    val categoryName: String?,
    val isDone: Boolean,
    val cardColor: Int?,
    val term: Date?,
)