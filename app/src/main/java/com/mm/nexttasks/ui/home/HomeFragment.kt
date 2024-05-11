package com.mm.nexttasks.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mm.nexttasks.TaskModel
import com.mm.nexttasks.TodoDatabaseHelper
import com.mm.nexttasks.databinding.FragmentHomeBinding
import com.mm.nexttasks.TaskListAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val taskModels: ArrayList<TaskModel> = ArrayList()

    private var todoDB: TodoDatabaseHelper? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.todoList

        todoDB = TodoDatabaseHelper(requireContext())

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
        val cursor = todoDB?.readAllData() ?: return

        if (cursor.count == 0) {
            Toast.makeText(this.requireContext(), "No data", Toast.LENGTH_SHORT).show()
            return
        }

        while (cursor.moveToNext()) {
            taskModels.add(
                TaskModel(
                    cursor.getString(1),
                    cursor.getInt(2) != 0,
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5),
                    cursor.getInt(6)
                )
            )
        }
    }
}
