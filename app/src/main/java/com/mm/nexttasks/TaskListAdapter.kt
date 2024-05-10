package com.mm.todoapp

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mm.nexttasks.R
import com.mm.nexttasks.TaskModel

class TaskListAdapter(val context: Context, val taskList: ArrayList<TaskModel>) : RecyclerView.Adapter<TaskListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : TaskListAdapter.MyViewHolder {
        // this is where you inflate the layout
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.task_card, parent, false)

        return TaskListAdapter.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskListAdapter.MyViewHolder, position: Int) {
        // assigning values to the views we created in the task_card layout file
        // based on the position of the recycler view

        holder.taskNameLabel.text = taskList[position].title
        holder.taskCategoryLabel.text = taskList[position].category
        holder.taskDeadlineLabel.text = taskList[position].termTimestamp.toString()
        holder.taskNameLabel.isChecked = taskList[position].isDone
//        holder.taskColorTab.setBackgroundColor((-16777216..-1).random())
        holder.taskColorTab.setBackgroundColor(taskList[position].cardColor)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskNameLabel = itemView.findViewById<CheckBox>(R.id.taskLabel)
        val taskCategoryLabel = itemView.findViewById<TextView>(R.id.taskCategory)
        val taskDeadlineLabel = itemView.findViewById<TextView>(R.id.taskDeadlineText)
        val taskColorTab = itemView.findViewById<View>(R.id.taskCardColor)
    }
}