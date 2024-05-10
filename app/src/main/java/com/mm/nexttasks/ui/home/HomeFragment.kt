package com.mm.nexttasks.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mm.nexttasks.R
import com.mm.nexttasks.TaskModel
import com.mm.nexttasks.databinding.FragmentHomeBinding
import com.mm.todoapp.TaskListAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val taskModels: ArrayList<TaskModel> = ArrayList()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.todoList

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
        val taskNames = resources.getStringArray(R.array.task_names)
        val taskCategories = resources.getStringArray(R.array.task_categories)
        val taskDeadlines = resources.getStringArray(R.array.task_deadlines)

//        for (i in taskNames.indices) {
//            taskModels.add(TaskModel(taskNames[i], taskCategories[i], taskDeadlines[i], false))
//        }
    }
}
