package com.mm.nexttasks

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mm.nexttasks.databinding.TaskCardBinding

class TaskListAdapter(private val context: Context, private val taskList: ArrayList<TaskModel>) : RecyclerView.Adapter<TaskListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : MyViewHolder {
        // this is where you inflate the layout
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.task_card, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // assigning values to the views we created in the task_card layout file
        // based on the position of the recycler view

        holder.binding.taskLabel.text = taskList[position].title
        holder.binding.taskCategory.text = taskList[position].category
        holder.binding.taskDeadlineText.text = taskList[position].termTimestamp.toString()
        holder.binding.taskLabel.isChecked = taskList[position].isDone
//        holder.taskColorTab.setBackgroundColor((-16777216..-1).random())
        holder.binding.taskCardColor.setBackgroundColor(taskList[position].cardColor)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = TaskCardBinding.bind(itemView)
    }
}