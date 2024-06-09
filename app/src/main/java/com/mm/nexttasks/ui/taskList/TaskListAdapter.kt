package com.mm.nexttasks.ui.taskList

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mm.nexttasks.MainApp
import com.mm.nexttasks.R
import com.mm.nexttasks.databinding.TaskCardBinding
import com.mm.nexttasks.db.views.TaskDetails
import java.text.DateFormat
import java.util.Locale

class TaskListAdapter(private val context: Context, private val taskList: ArrayList<TaskDetails>)
    : RecyclerView.Adapter<TaskListAdapter.MyViewHolder>() {

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
        holder.binding.taskCategory.text = taskList[position].categoryName
        holder.binding.taskDeadlineText.text = taskList[position].term.toString()
        holder.binding.taskLabel.isChecked = taskList[position].isDone
        holder.binding.taskCardColor.setBackgroundColor(taskList[position].cardColor ?: 2131100398)

        val taskTerm = taskList[position].term
        if (taskTerm == null) {
            holder.binding.taskDeadlineLine.visibility = View.GONE
        } else {
            @SuppressLint("SetTextI18n")
            holder.binding.taskDeadlineText.text = (
                    DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault(Locale.Category.FORMAT)).format(taskTerm)
                    + ", "
                    + DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault(Locale.Category.FORMAT)).format(taskTerm)
                    )
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    fun getTaskList(): ArrayList<TaskDetails> {
        return taskList
    }

    fun removeAt(position: Int) {
        taskList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun insertAt(position: Int, task: TaskDetails) {
        taskList.add(position, task)
        notifyItemInserted(position)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = TaskCardBinding.bind(itemView)
    }
}