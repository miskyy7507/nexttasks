package com.mm.nexttasks.ui.taskList

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
import com.mm.nexttasks.databinding.FragmentHomeBinding
import com.mm.nexttasks.db.AppDatabase
import com.mm.nexttasks.db.dao.TaskDao
import com.mm.nexttasks.db.views.TaskDetails


class TaskListFragment(selectedItemNames: String?) : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var _database: AppDatabase? = null
    private val database get() = _database!!
    private var taskDao: TaskDao? = null

    private val taskModels: ArrayList<TaskDetails> = ArrayList()

    private val selectedItemName = selectedItemNames

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

        setUpTaskListModels(selectedItemName)
        
        val adapter = TaskListAdapter(requireContext(), taskModels)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        val swipeHandler = object : SwipeCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val taskToRemove = adapter.getTaskList()[position]

                adapter.removeAt(position)

                Snackbar.make(requireView(), "Zadanie usunięte", Snackbar.LENGTH_LONG)
                    .setAction("Cofnij") {
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
                                    taskDao!!.delete(taskDao!!.getTaskFromId(taskToRemove.taskId))
                                }

                                BaseCallback.DISMISS_EVENT_ACTION -> {
                                    // "undo" button pressed, so do nothing
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
        val tasks: List<TaskDetails>?
        if (selectedItemName != null) {
            tasks = taskDao!!.getTasksFromList(selectedItemName)
        } else {
            tasks = taskDao!!.getAll()
        }
        if (tasks.isNotEmpty()) {
            binding.noTasksTip.visibility = View.GONE
        }
        for (task in tasks) {
            taskModels.add(task)
        }
    }
}
