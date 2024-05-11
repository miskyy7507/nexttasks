package com.mm.nexttasks

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mm.nexttasks.databinding.ActivityTaskEditBinding

class TaskEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTaskEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val titleInput = binding.taskTitleInput
        val taskDoneCheckbox = binding.taskDoneCheckbox
        val categoryInput = binding.taskCategoryInput
        val priorityInput = binding.taskPriorityInput
        val taskTermInput = binding.taskTermInput
        val addButton = binding.addButton



        addButton.setOnClickListener {
            val task = TaskModel(
                titleInput.text.toString().trim(),
                taskDoneCheckbox.isChecked,
                categoryInput.text.toString().trim(),
                priorityInput.text.toString().trim(),
                getColor(R.color.task_tab_color_yellow),
                taskTermInput.text.toString().toInt()
            )

            val db = TodoDatabaseHelper(it.context)
            val result = db.addTask(task)

            if (result != -1L) {
                Toast.makeText(this, "Dodano zadanie", Toast.LENGTH_SHORT).show()
            }
        }
    }

}