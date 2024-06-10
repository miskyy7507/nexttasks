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
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.mm.nexttasks.databinding.ActivityTaskEditBinding
import com.mm.nexttasks.db.entities.*
import java.util.Calendar
import java.util.Locale

class TaskEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskEditBinding

    private lateinit var titleInput: TextInputEditText
    private lateinit var taskDoneCheckbox: CheckBox
    private lateinit var colorPickerRadios: RadioGroup
    private lateinit var datePickerInput: TextInputEditText
    private lateinit var timePickerInput: TextInputEditText
    private lateinit var categorySpinner: Spinner
    private lateinit var prioritySpinner: Spinner
    private lateinit var taskListSpinner: Spinner

    private lateinit var taskDeadlineCalendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appDatabase = MainApp.database!!
        val taskDao = appDatabase.taskDao()
        val categoryDao = appDatabase.categoryDao()
        val priorityDao = appDatabase.priorityDao()
        val taskListDao = appDatabase.taskListDao()

        val taskIdToEdit = intent.extras?.getLong("taskIdToEdit")
        val taskToEdit = if (taskIdToEdit != null) taskDao.getTaskFromId(taskIdToEdit) else null

        if (taskToEdit != null) {
            supportActionBar?.title = getString(R.string.task_edit_edit_mode)
        } else {
            supportActionBar?.title = getString(R.string.task_edit_add_mode)
        }

        binding = ActivityTaskEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        titleInput = binding.taskTitleInput
        taskDoneCheckbox = binding.taskDoneCheckbox
        colorPickerRadios = binding.colorPicker
        datePickerInput = binding.taskDateInput
        timePickerInput = binding.taskTimeInput
        categorySpinner = binding.taskCategorySpinner
        prioritySpinner = binding.taskPrioritySpinner
        taskListSpinner = binding.taskListPicker

        val categories = (
                listOf(Category(0, getText(R.string.none).toString()))
                        + categoryDao.getAll()
                        + listOf(Category(-1, getText(R.string.new_category_item).toString()))
                ).toMutableList()
        val categorySpinnerAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
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
                if (position == categories.size - 1) { // last item is the "New..." item
                    categorySpinner.setSelection(categorySpinnerPosition)
                    val alertDialog = AlertDialog.Builder(binding.root.context)
                    alertDialog.setTitle(getText(R.string.new_category_add_dialog))

                    val input = EditText(binding.root.context)
                    input.inputType = InputType.TYPE_CLASS_TEXT
                    alertDialog.setView(input)

                    alertDialog.setPositiveButton(getText(R.string.add)) { dialog, _ ->
                        val newItem = input.text.toString()
                        if (newItem.isNotBlank()) {
                            val newlyInsertedCategoryId = categoryDao.insert(Category(0, newItem))
                            categories.add(
                                categories.size - 1,
                                Category(newlyInsertedCategoryId, newItem)
                            ) // Add new item before "Add New Item" special item
                            categorySpinnerAdapter.notifyDataSetChanged()
                            dialog.dismiss()
//                            categorySpinner.setSelection(categories.size - 1) // Set spinner selection at the newly added item (atm it causes problems)
                        }
                    }

                    alertDialog.setNegativeButton(getText(R.string.cancel)) { dialog, _ -> dialog.cancel() }

                    alertDialog.show()
                } else {
                    categorySpinnerPosition = categorySpinner.selectedItemPosition
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {} // Do nothing
        }

        val priorities = (
                listOf(Priority(0, getText(R.string.none).toString()))
                        + priorityDao.getAll()
                        + listOf(Priority(-1, getText(R.string.new_priority_item).toString()))
                ).toMutableList()
        val prioritySpinnerAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, priorities)
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
                if (position == priorities.size - 1) { // last item is the "New..." item
                    prioritySpinner.setSelection(prioritySpinnerPosition)
                    val alertDialog = AlertDialog.Builder(binding.root.context)
                    alertDialog.setTitle(getText(R.string.new_category_add_dialog))

                    val input = EditText(binding.root.context)
                    input.inputType = InputType.TYPE_CLASS_TEXT
                    alertDialog.setView(input)

                    alertDialog.setPositiveButton(getText(R.string.add)) { dialog, _ ->
                        val newItem = input.text.toString()
                        if (newItem.isNotBlank()) {
                            val newlyInsertedPriorityId = priorityDao.insert(Priority(0, newItem))
                            priorities.add(
                                priorities.size - 1,
                                Priority(newlyInsertedPriorityId, newItem)
                            ) // Add new item before "Add New Item" special item
                            prioritySpinnerAdapter.notifyDataSetChanged()
                            dialog.dismiss()
                        }
                    }

                    alertDialog.setNegativeButton(getText(R.string.cancel)) { dialog, _ -> dialog.cancel() }

                    alertDialog.show()
                } else {
                    prioritySpinnerPosition = prioritySpinner.selectedItemPosition
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {} // Do nothing
        }

        val taskLists = taskListDao.getAll()
        val taskListSpinnerAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, taskLists)
        taskListSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        taskListSpinner.adapter = taskListSpinnerAdapter

        taskDeadlineCalendar = Calendar.getInstance()
        taskDeadlineCalendar.add(Calendar.DAY_OF_MONTH, 1)
        taskDeadlineCalendar.set(Calendar.HOUR_OF_DAY, 23)
        taskDeadlineCalendar.set(Calendar.MINUTE, 59)
        taskDeadlineCalendar.set(Calendar.SECOND, 59)
        taskDeadlineCalendar.set(Calendar.MILLISECOND, 0)

        if (taskToEdit != null) {
            titleInput.setText(taskToEdit.title)
            taskDoneCheckbox.isChecked = taskToEdit.isDone

            if (taskToEdit.categoryId != null) {
                categorySpinner.setSelection(categorySpinnerAdapter.getPosition(categoryDao.getFromId(taskToEdit.categoryId)))
            } else {
                categorySpinner.setSelection(0)
            }

            if (taskToEdit.priorityId != null) {
                prioritySpinner.setSelection(prioritySpinnerAdapter.getPosition(priorityDao.getFromId(taskToEdit.priorityId)))
            } else {
                prioritySpinner.setSelection(0)
            }

            taskListSpinner.setSelection(taskListSpinnerAdapter.getPosition(taskListDao.getFromId(taskToEdit.taskListId)))

            taskDeadlineCalendar.time = taskToEdit.term!!
        }

        datePickerInput.setText(
            java.text.DateFormat.getDateInstance(
                java.text.DateFormat.SHORT, Locale.getDefault(
                    Locale.Category.FORMAT
                )
            ).format(taskDeadlineCalendar.time)
        )
        timePickerInput.setText(
            java.text.DateFormat.getTimeInstance(
                java.text.DateFormat.SHORT, Locale.getDefault(
                    Locale.Category.FORMAT
                )
            ).format(taskDeadlineCalendar.time)
        )

        datePickerInput.inputType = InputType.TYPE_NULL
        datePickerInput.setOnClickListener {
            DatePickerFragment().show(supportFragmentManager, "datePicker")
        }

        timePickerInput.inputType = InputType.TYPE_NULL
        timePickerInput.setOnClickListener {
            TimePickerFragment().show(supportFragmentManager, "timePicker")
        }

        binding.addButton.setOnClickListener {
            binding.addButton.isClickable = false
            val taskName = titleInput.text?.trim().toString()
            val taskListId =
                taskListSpinnerAdapter.getItem(taskListSpinner.selectedItemPosition)!!.taskListId
            var priorityId: Long? =
                prioritySpinnerAdapter.getItem(prioritySpinner.selectedItemPosition)!!.priorityId
            if (priorityId == 0L) {
                priorityId = null
            }
            var categoryId: Long? =
                categorySpinnerAdapter.getItem(categorySpinner.selectedItemPosition)!!.categoryId
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

            val term = taskDeadlineCalendar.time


            val taskToAdd = Task(
                taskToEdit?.taskId ?: 0,
                taskName,
                taskListId,
                priorityId,
                categoryId,
                isDone,
                getColor(colorChosen),
                term
            )

            if (taskToEdit != null) {
                taskDao.editTask(taskToAdd)
            } else {
                taskDao.insert(taskToAdd)
            }

            onSupportNavigateUp()
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
            val listener = requireActivity() as TaskEditActivity
            listener.taskDeadlineCalendar.set(Calendar.YEAR, year)
            listener.taskDeadlineCalendar.set(Calendar.MONTH, month)
            listener.taskDeadlineCalendar.set(Calendar.DAY_OF_MONTH, day)
            listener.datePickerInput.setText(
                java.text.DateFormat.getDateInstance(
                    java.text.DateFormat.SHORT, Locale.getDefault(
                        Locale.Category.FORMAT)).format(listener.taskDeadlineCalendar.time))
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
            val listener = requireActivity() as TaskEditActivity
            listener.taskDeadlineCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            listener.taskDeadlineCalendar.set(Calendar.MINUTE, minute)
            listener.timePickerInput.setText(
                java.text.DateFormat.getTimeInstance(
                    java.text.DateFormat.SHORT, Locale.getDefault(
                        Locale.Category.FORMAT)).format(listener.taskDeadlineCalendar.time))
        }
    }

}