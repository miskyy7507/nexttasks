package com.mm.nexttasks

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.mm.nexttasks.databinding.ActivityTaskEditBinding
import com.mm.nexttasks.db.entities.Category
import com.mm.nexttasks.db.entities.Priority

class TaskEditActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityTaskEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appDatabase = MainApp.database!!
        val categoryDao = appDatabase.categoryDao()
        val priorityDao = appDatabase.priorityDao()
        val taskListDao = appDatabase.taskListDao()

        binding = ActivityTaskEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

//        val titleInput = binding.taskTitleInput
//        val taskDoneCheckbox = binding.taskDoneCheckbox
//        val categoryInput = binding.taskCategoryInput
//        val priorityInput = binding.taskPriorityInput
//        val taskTermInput = binding.taskTermInput
        val addButton = binding.addButton



        addButton.setOnClickListener {

        }
    }

}