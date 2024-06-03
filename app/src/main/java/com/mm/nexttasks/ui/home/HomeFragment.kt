package com.mm.nexttasks.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mm.nexttasks.MainApp
import com.mm.nexttasks.databinding.FragmentHomeBinding
import com.mm.nexttasks.db.views.TaskDetails
import com.mm.nexttasks.TaskListAdapter
import com.mm.nexttasks.db.AppDatabase
import com.mm.nexttasks.db.dao.TaskDao

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var _database: AppDatabase? = null
    private val database get() = _database!!
    private var taskDao: TaskDao? = null

    private val taskModels: ArrayList<TaskDetails> = ArrayList()

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

        setUpTaskListModels()
        
        val adapter = TaskListAdapter(this.requireContext(), taskModels)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpTaskListModels() {
        val tasks = taskDao!!.getAll()
        if (tasks.isEmpty()) {
            Toast.makeText(requireContext(), "Empty task list", Toast.LENGTH_LONG).show()
        }
        for (task in tasks) {
            taskModels.add(task)
        }
    }
}
