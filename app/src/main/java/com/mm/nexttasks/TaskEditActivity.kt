package com.mm.nexttasks

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.InputType
import android.text.format.DateFormat
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.mm.nexttasks.databinding.ActivityTaskEditBinding
import com.mm.nexttasks.db.entities.*
import java.util.Calendar
import java.util.Date


class TaskEditActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityTaskEditBinding

    public lateinit var datePickerInput: TextInputEditText
    public lateinit var timePickerInput: TextInputEditText

    private val date: Date = Date()

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
        val categories = (listOf(Category(0, "Brak")) + categoryDao.getAll() + listOf(Category(-1, "Nowa kategoria..."))).toMutableList()
        val categorySpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categorySpinnerAdapter

        var categorySpinnerPosition = categorySpinner.selectedItemPosition

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == categories.size -1) { // last item is the "New..." item
                    categorySpinner.setSelection(categorySpinnerPosition)
                    val builder = AlertDialog.Builder(binding.root.context)
                    builder.setTitle("Dodaj nową kategorie")

                    val input = EditText(binding.root.context)
                    input.inputType = InputType.TYPE_CLASS_TEXT
                    builder.setView(input)

                    builder.setPositiveButton("Dodaj") { dialog, _ ->
                        val newItem = input.text.toString()
                        if (newItem.isNotBlank()) {
                            val newlyInsertedCategoryId = categoryDao.insert(Category(0, newItem))
                            categories.add(categories.size -1, Category(newlyInsertedCategoryId, newItem)) // Add new item before "Add New Item" special item
                            categorySpinnerAdapter.notifyDataSetChanged()
                            dialog.dismiss()
//                            categorySpinner.setSelection(categories.size - 1) // Set spinner selection at the newly added item (atm it causes problems)
                        }
                    }

                    builder.setNegativeButton("Anuluj") { dialog, _ -> dialog.cancel() }

                    builder.show()
                } else {
                    categorySpinnerPosition = categorySpinner.selectedItemPosition
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {} // Do nothing
        }

        val prioritySpinner = binding.taskPrioritySpinner
        val priorities = (listOf(Priority(0, "Brak priorytetu")) + priorityDao.getAll() + listOf(Priority(-1, "Nowy rodzaj priorytetu..."))).toMutableList()
        val prioritySpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, priorities)
        prioritySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        prioritySpinner.adapter = prioritySpinnerAdapter

        var prioritySpinnerPosition = prioritySpinner.selectedItemPosition

        prioritySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == priorities.size -1) { // last item is the "New..." item
                    prioritySpinner.setSelection(prioritySpinnerPosition)
                    val builder = AlertDialog.Builder(binding.root.context)
                    builder.setTitle("Dodaj nowy rodzaj priorytetu")

                    val input = EditText(binding.root.context)
                    input.inputType = InputType.TYPE_CLASS_TEXT
                    builder.setView(input)

                    builder.setPositiveButton("Dodaj") { dialog, _ ->
                        val newItem = input.text.toString()
                        if (newItem.isNotBlank()) {
                            val newlyInsertedPriorityId = priorityDao.insert(Priority(0, newItem))
                            priorities.add(priorities.size -1, Priority(newlyInsertedPriorityId, newItem)) // Add new item before "Add New Item" special item
                            prioritySpinnerAdapter.notifyDataSetChanged()
                            dialog.dismiss()
                        }
                    }

                    builder.setNegativeButton("Anuluj") { dialog, _ -> dialog.cancel() }

                    builder.show()
                } else {
                    prioritySpinnerPosition = prioritySpinner.selectedItemPosition
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {} // Do nothing
        }

        val taskListSpinner = binding.taskListPicker
        val taskLists = taskListDao.getAll()
        val taskListSpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, taskLists)
        taskListSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        taskListSpinner.adapter = taskListSpinnerAdapter

        binding.addButton.setOnClickListener {
            val taskName = titleInput.text?.trim().toString()
            val taskListId = taskListSpinnerAdapter.getItem(taskListSpinner.selectedItemPosition)!!.taskListId
            var priorityId: Long? = prioritySpinnerAdapter.getItem(prioritySpinner.selectedItemPosition)!!.priorityId
            if (priorityId == 0L) {
                priorityId = null
            }
            var categoryId: Long? = categorySpinnerAdapter.getItem(categorySpinner.selectedItemPosition)!!.categoryId
            if (categoryId == 0L) {
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

            val taskToAdd = Task(0, taskName, taskListId, priorityId, categoryId, isDone, getColor(colorChosen), null)

            taskDao.insert(taskToAdd)

            Toast.makeText(this, "Added new task", Toast.LENGTH_LONG).show()

        }

        datePickerInput = binding.taskDateInput
        datePickerInput.inputType = InputType.TYPE_NULL
        datePickerInput.setOnClickListener {
            DatePickerFragment().show(supportFragmentManager, "datePicker")
        }

        timePickerInput = binding.taskTimeInput
        timePickerInput.inputType = InputType.TYPE_NULL
        timePickerInput.setOnClickListener {
            TimePickerFragment().show(supportFragmentManager, "timePicker")
        }

    }

    class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Use the current date as the default date in the picker.
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // Create a new instance of DatePickerDialog and return it.
            return DatePickerDialog(requireContext(), this, year, month, day)

        }

        override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
            val listener = activity as? TaskEditActivity
            listener?.datePickerInput?.setText(day.toString() + "." + (month + 1) + "." + year)
        }
    }


    class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Use the current time as the default values for the picker.
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            // Create a new instance of TimePickerDialog and return it.
            return TimePickerDialog(requireContext(), this, hour, minute, DateFormat.is24HourFormat(activity))
        }

        override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
            val listener = activity as? TaskEditActivity
            listener?.timePickerInput?.setText("$hourOfDay:$minute")
        }
    }


}