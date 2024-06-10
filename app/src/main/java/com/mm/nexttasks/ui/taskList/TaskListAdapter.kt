package com.mm.nexttasks.ui.taskList

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mm.nexttasks.MainApp
import com.mm.nexttasks.R
import com.mm.nexttasks.databinding.TaskCardBinding
import com.mm.nexttasks.databinding.TaskListSeparatorItemBinding
import com.mm.nexttasks.db.views.TaskDetails
import java.text.DateFormat
import java.util.Locale

private const val VIEW_TYPE_ITEM = 1
private const val VIEW_TYPE_SEPARATOR = 2

class TaskListAdapter(private val context: Context, private val taskList: ArrayList<TaskListItem>, private val listener: OnItemClickListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder {
        // this is where you inflate the layout
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val inflater = LayoutInflater.from(context)
                val view = inflater.inflate(R.layout.task_card, parent, false)
                TaskCardViewHolder(view)
            }
            VIEW_TYPE_SEPARATOR -> {
                val inflater = LayoutInflater.from(context)
                val view = inflater.inflate(R.layout.task_list_separator_item, parent, false)
                TaskListSeparatorViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // assigning values to the views we created in the task_card layout file
        // based on the position of the recycler view
        when (holder) {
            is TaskCardViewHolder -> {
                val taskItem = taskList[position] as TaskListItem.TaskCardItem
                holder.binding.taskLabel.text = taskItem.taskDetails.title
                if (taskItem.taskDetails.categoryName == null) {
                    holder.binding.separator2.visibility = View.GONE
                }
                holder.binding.taskCategory.text = taskItem.taskDetails.categoryName
                holder.binding.taskList.text = context.getString(R.string.task_name_view_template, taskItem.taskDetails.taskListName)
                holder.binding.taskDeadlineText.text = taskItem.taskDetails.toString()
                holder.binding.taskLabel.isChecked = taskItem.taskDetails.isDone
                holder.binding.taskCardColor.setBackgroundColor(taskItem.taskDetails.cardColor ?: 2131100398)
                if (holder.binding.taskLabel.isChecked) {
                    holder.binding.taskLabel.paintFlags = (holder.binding.taskLabel.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
                } else {
                    holder.binding.taskLabel.paintFlags = (holder.binding.taskLabel.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
                }


                val taskTerm = taskItem.taskDetails.term
                if (taskTerm == null) {
                    holder.binding.taskDeadlineLine.visibility = View.GONE
                } else {
                    holder.binding.taskDeadlineText.text = (
                        context.getString(
                            R.string.date_and_time_task_list_template,
                            DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault(Locale.Category.FORMAT)).format(taskTerm),
                            DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault(Locale.Category.FORMAT)).format(taskTerm)
                        )
                    )
                }

                holder.binding.taskLabel.setOnCheckedChangeListener { buttonView, isChecked ->
                    val database = MainApp.database!!
                    val taskDao = database.taskDao()
                    taskDao.updateDoneState(taskItem.taskDetails.taskId, isChecked)
                    if (isChecked) {
                        holder.binding.taskLabel.paintFlags = (holder.binding.taskLabel.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
                    } else {
                        holder.binding.taskLabel.paintFlags = (holder.binding.taskLabel.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
                    }
                }

                holder.itemView.setOnClickListener {
                    listener.onItemClick(taskItem)
                }
            }
            is TaskListSeparatorViewHolder -> {
                val separatorItem = taskList[position] as TaskListItem.TaskListSeparatorItem
                holder.binding.textView4.text = separatorItem.text
            }
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (taskList[position]) {
            is TaskListItem.TaskCardItem -> VIEW_TYPE_ITEM
            is TaskListItem.TaskListSeparatorItem -> VIEW_TYPE_SEPARATOR
        }
    }

    fun getTaskList(): ArrayList<TaskListItem> {
        return taskList
    }

    fun removeAt(position: Int) {
        taskList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun insertAt(position: Int, item: TaskListItem) {
        taskList.add(position, item)
        notifyItemInserted(position)
    }

    interface OnItemClickListener {
        fun onItemClick(item: TaskListItem.TaskCardItem)
    }

    class TaskCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = TaskCardBinding.bind(itemView)
    }

    class TaskListSeparatorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = TaskListSeparatorItemBinding.bind(itemView)
    }
}