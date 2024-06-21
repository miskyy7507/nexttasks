package com.mm.nexttasks

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.widget.EditText
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import com.mm.nexttasks.databinding.ActivityMainBinding
import com.mm.nexttasks.db.entities.TaskList
import com.mm.nexttasks.ui.calendar.CalendarFragment
import com.mm.nexttasks.ui.taskList.TaskListFragment

private const val NAV_MENU_ADD_NEW_TASK_LIST_ITEM_ID = -1
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    private var database = DatabaseProvider.getDatabase(this)
    private var taskListDao = database.taskListDao()

    private var calendarViewToggle = false
    private var savedTaskList: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = binding.appBarMain.toolbar
        drawerLayout = binding.drawerLayout
        navView = binding.navView

        setSupportActionBar(toolbar)

        binding.appBarMain.fab.setOnClickListener {
            val intent = Intent(this, TaskEditActivity::class.java)
            startActivity(intent)
        }

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        populateNavMenu()
    }

    private fun populateNavMenu() {
        val taskListsList = taskListDao.getAll()

        val allTaskListsMenuItem = navView.menu.add(1, 0, Menu.NONE, getText(R.string.task_list_show_all)).setIcon(R.drawable.checklist_40dp)
        for (list in taskListsList) {
            navView.menu.add(1, list.taskListId.toInt(), Menu.NONE, list.name)
        }
        navView.menu.add(2, NAV_MENU_ADD_NEW_TASK_LIST_ITEM_ID, Menu.NONE, getString(R.string.add_new_task_list)).setIcon(R.drawable.plus_32)
        navView.menu.setGroupCheckable(1, true, true)
        allTaskListsMenuItem.setChecked(true)
        supportActionBar?.title = getText(R.string.task_list_show_all)

        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_content_main, TaskListFragment.newInstance(savedTaskList))
            .commit()

        // Set up the click listener for the navigation drawer items
        navView.setNavigationItemSelectedListener { menuItem ->
            if (menuItem.itemId == NAV_MENU_ADD_NEW_TASK_LIST_ITEM_ID) {
                val alertDialog = AlertDialog.Builder(binding.root.context)
                alertDialog.setTitle(getString(R.string.add_new_task_list))

                val input = EditText(binding.root.context)
                input.inputType = InputType.TYPE_CLASS_TEXT
                alertDialog.setView(input)

                alertDialog.setPositiveButton(getText(R.string.add)) { dialog, _ ->
                    val newTaskListName = input.text.toString()
                    if (newTaskListName.isNotBlank()) {
                        navView.menu.removeItem(NAV_MENU_ADD_NEW_TASK_LIST_ITEM_ID)
                        val newlyInsertedTaskListId = taskListDao.insert(TaskList(0, newTaskListName))
                        navView.menu.add(Menu.FIRST, newlyInsertedTaskListId.toInt(), Menu.NONE, newTaskListName)
                        navView.menu.add(2, NAV_MENU_ADD_NEW_TASK_LIST_ITEM_ID, Menu.NONE, getString(R.string.add_new_task_list))
                        dialog.dismiss()
                    }
                }

                alertDialog.setNegativeButton(getText(R.string.cancel)) { dialog, _ -> dialog.cancel() }

                alertDialog.show()
                return@setNavigationItemSelectedListener false
            }
            // Get the selected item from the database
            val selectedItem = menuItem.title.toString()
            supportActionBar?.title = selectedItem
            savedTaskList = if (menuItem.itemId == allTaskListsMenuItem.itemId) {null} else selectedItem

            // Update the fragment with the selected item's information
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, TaskListFragment.newInstance(savedTaskList))
                .commit()

            drawerLayout.close()
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId != R.id.action_calendar_view_toggle) {
            return super.onOptionsItemSelected(item)
        }

        calendarViewToggle = !calendarViewToggle
        if (calendarViewToggle) {
            item.icon = getDrawable(R.drawable.checklist_40dp)
            item.title = getText(R.string.task_list_view)
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, CalendarFragment())
                .commit()
        } else {
            item.icon = getDrawable(R.drawable.calendar_month_40dp)
            item.title = getText(R.string.calendar_view)
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, TaskListFragment.newInstance(savedTaskList))
                .commit()
            supportActionBar?.title = savedTaskList ?: getText(R.string.task_list_show_all)
        }

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}