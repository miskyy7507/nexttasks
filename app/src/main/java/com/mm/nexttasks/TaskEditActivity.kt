package com.mm.nexttasks

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.ui.AppBarConfiguration
import com.mm.nexttasks.databinding.ActivityTaskEditBinding
import com.mm.nexttasks.db.entities.Category

class TaskEditActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityTaskEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val categoryDao = MainApp.database!!.categoryDao()

        binding = ActivityTaskEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categorySpinner = binding.taskCategorySpinner
        val categories = categoryDao.getAll()

        val adapter = ArrayAdapter(this@TaskEditActivity, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        val titleInput = binding.taskTitleInput
        val taskDoneCheckbox = binding.taskDoneCheckbox
        val categoryInput = binding.taskCategoryInput
        val priorityInput = binding.taskPriorityInput
        val taskTermInput = binding.taskTermInput
        val addButton = binding.addButton



        addButton.setOnClickListener {

        }
    }

}