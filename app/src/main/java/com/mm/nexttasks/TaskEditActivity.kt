package com.mm.nexttasks

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.mm.nexttasks.databinding.ActivityTaskEditBinding
import com.mm.nexttasks.db.entities.*

class TaskEditActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityTaskEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appDatabase = MainApp.database!!
        val taskDao = appDatabase.taskDao()
        val categoryDao = appDatabase.categoryDao()
        val priorityDao = appDatabase.priorityDao()
        val taskListDao = appDatabase.taskListDao()

        binding = ActivityTaskEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val titleInput = binding.taskTitleInput
        val taskDoneCheckbox = binding.taskDoneCheckbox
        val colorPickerRadios = binding.colorPicker

        val categorySpinner = binding.taskCategorySpinner
        val categories = listOf(Category(0, "Brak kategorii")) + categoryDao.getAll()
        val categorySpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categorySpinnerAdapter

        val prioritySpinner = binding.taskPrioritySpinner
        val priorities = listOf(Priority(0, "Brak priorytetu")) + priorityDao.getAll()
        val prioritySpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, priorities)
        prioritySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        prioritySpinner.adapter = prioritySpinnerAdapter

        val taskListSpinner = binding.taskListPicker
        val taskLists = taskListDao.getAll()
        val taskListSpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, taskLists)
        taskListSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        taskListSpinner.adapter = taskListSpinnerAdapter

        binding.addButton.setOnClickListener {
            val taskName = titleInput.text.trim().toString()
            val taskListId = taskListSpinnerAdapter.getItem(taskListSpinner.selectedItemPosition)!!.taskListId
            var priorityId: Int? = prioritySpinnerAdapter.getItem(prioritySpinner.selectedItemPosition)!!.priorityId
            if (priorityId == 0) {
                priorityId = null
            }
            var categoryId: Int? = categorySpinnerAdapter.getItem(categorySpinner.selectedItemPosition)!!.categoryId
            if (categoryId == 0) {
                categoryId = null
            }
            val isDone = taskDoneCheckbox.isChecked

            val colorChosen = when (colorPickerRadios.checkedRadioButtonId) {
                R.id.colorRedChoice -> R.color.task_tab_color_red
                R.id.colorYellowChoice -> R.color.task_tab_color_yellow
                R.id.colorGreenChoice -> R.color.task_tab_color_green
                R.id.colorBlueChoice -> R.color.task_tab_color_blue
                else -> R.color.task_tab_color_gray
            }

            // val term = TODO

            val taskToAdd = Task(0, taskName, taskListId, priorityId, categoryId, isDone, getColor(colorChosen), null)

            taskDao.insert(taskToAdd)

            Toast.makeText(this, "Added new task", Toast.LENGTH_LONG).show()

        }
    }

}