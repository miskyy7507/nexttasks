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
        val colorPickerRadios = binding.colorPicker


        addButton.setOnClickListener {
            val colorChosen = when (colorPickerRadios.checkedRadioButtonId) {
                R.id.colorRedChoice -> R.color.task_tab_color_red
                R.id.colorYellowChoice -> R.color.task_tab_color_yellow
                R.id.colorGreenChoice -> R.color.task_tab_color_green
                R.id.colorBlueChoice -> R.color.task_tab_color_blue
                else -> R.color.task_tab_color_gray
            }

            val task = TaskModel(
                titleInput.text.toString().trim(),
                taskDoneCheckbox.isChecked,
                categoryInput.text.toString().trim(),
                priorityInput.text.toString().trim(),
                getColor(colorChosen),
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