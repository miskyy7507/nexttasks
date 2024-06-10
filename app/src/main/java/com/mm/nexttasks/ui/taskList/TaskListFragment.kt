package com.mm.nexttasks.ui.taskList

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar.BaseCallback
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.Callback
import com.mm.nexttasks.MainApp
import com.mm.nexttasks.R
import com.mm.nexttasks.TaskEditActivity
import com.mm.nexttasks.databinding.FragmentHomeBinding
import com.mm.nexttasks.db.AppDatabase
import com.mm.nexttasks.db.dao.TaskDao
import com.mm.nexttasks.db.views.TaskDetails
import java.text.DateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private const val VIEW_TYPE_ITEM = 1
private const val VIEW_TYPE_SEPARATOR = 2
class TaskListFragment : Fragment(), TaskListAdapter.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var _database: AppDatabase? = null
    private val database get() = _database!!
    private var taskDao: TaskDao? = null

    private val taskModels: ArrayList<TaskListItem> = ArrayList()

    companion object {
         fun newInstance(selectedTaskName: String?): TaskListFragment {
            val fragment = TaskListFragment()
            val b = Bundle()
            b.putString("selectedTaskName", selectedTaskName)
            fragment.arguments = b
            return fragment
        }
    }

    override fun onItemClick(item: TaskListItem.TaskCardItem) {
        val intent = Intent(requireContext(), TaskEditActivity::class.java)
        val b = Bundle()
        b.putLong("taskIdToEdit", item.taskDetails.taskId)
        intent.putExtras(b)
        startActivity(intent)
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.todoList

        _database = MainApp.database!!
        taskDao = database.taskDao()

        val selectedItemName = if (arguments != null) requireArguments().getString("selectedTaskName", null) else null

        setUpTaskListModels(selectedItemName)
        
        val adapter = TaskListAdapter(requireContext(), taskModels, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        val swipeHandler = object : SwipeCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val taskToRemove = adapter.getTaskList()[position] as TaskListItem.TaskCardItem
                val separatorItem = if (
                    adapter.getItemViewType(position - 1) == VIEW_TYPE_SEPARATOR &&
                    (adapter.itemCount == position + 1 ||
                    adapter.getItemViewType(position + 1) == VIEW_TYPE_SEPARATOR)
                ) {
                    // consider removing the separator if deleted item is the only one in the group
                    adapter.getTaskList()[position - 1] as TaskListItem.TaskListSeparatorItem
                } else null

                adapter.removeAt(position)
                if (separatorItem != null) {
                    adapter.removeAt(position - 1)
                }

                Snackbar.make(requireView(), getText(R.string.task_deleted_info), Snackbar.LENGTH_LONG)
                    .setAction(getText(R.string.undo)) {
                        if (separatorItem != null) {
                            adapter.insertAt(position - 1, separatorItem)
                        }
                        adapter.insertAt(position, taskToRemove)
                        recyclerView.scrollToPosition(position)
                    }
                    .setActionTextColor(Color.YELLOW)
                    .addCallback(object : Callback() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            when (event) {
                                BaseCallback.DISMISS_EVENT_SWIPE, BaseCallback.DISMISS_EVENT_TIMEOUT,
                                BaseCallback.DISMISS_EVENT_MANUAL, BaseCallback.DISMISS_EVENT_CONSECUTIVE -> {
                                    // remove task from database only when snackbar with undo button is gone
                                    taskDao!!.delete(taskDao!!.getTaskFromId(taskToRemove.taskDetails.taskId))
                                }

                                BaseCallback.DISMISS_EVENT_ACTION -> {
                                    // action button (the "undo" button here) has been pressed, so do nothing
                                }
                            }
                        }
                    })
                    .show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpTaskListModels(selectedItemName: String?) {
        val tasks = if (selectedItemName != null) {
            taskDao!!.getTasksFromList(selectedItemName)
        } else {
            taskDao!!.getAll()
        }
        val tasksDateSorted = tasks.sortedWith(compareBy(nullsLast()) { it.term })
        if (tasks.isNotEmpty()) {
            binding.noTasksTip.visibility = View.GONE
        }
        val lastTaskDate = Calendar.getInstance()
        lastTaskDate.time = Date(0)
        var noDateSetSeparator = false
        for (task in tasksDateSorted) {
            if (task.term == null) {
                if (!noDateSetSeparator) {
                    taskModels.add(TaskListItem.TaskListSeparatorItem(getText(R.string.unspecified_separator_text)))
                    noDateSetSeparator = true
                }
                taskModels.add(TaskListItem.TaskCardItem(task))
            } else {
                val currentTaskDate = Calendar.getInstance()
                currentTaskDate.time = task.term
                if (!(
                            currentTaskDate.get(Calendar.YEAR) == lastTaskDate.get(Calendar.YEAR) &&
                                    currentTaskDate.get(Calendar.MONTH) == lastTaskDate.get(Calendar.MONTH) &&
                                    currentTaskDate.get(Calendar.DAY_OF_MONTH) == lastTaskDate.get(Calendar.DAY_OF_MONTH)
                            )) {
                    taskModels.add(TaskListItem.TaskListSeparatorItem(DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault(Locale.Category.FORMAT)).format(currentTaskDate.time)))
                }
                lastTaskDate.time = currentTaskDate.time
                taskModels.add(TaskListItem.TaskCardItem(task))
            }
        }
    }
}