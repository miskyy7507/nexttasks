package com.mm.nexttasks.ui.taskList

import com.mm.nexttasks.db.views.TaskDetails

sealed class TaskListItem {
    data class TaskCardItem(val taskDetails: TaskDetails) : TaskListItem()
    data class TaskListSeparatorItem(val text: CharSequence) : TaskListItem()
}