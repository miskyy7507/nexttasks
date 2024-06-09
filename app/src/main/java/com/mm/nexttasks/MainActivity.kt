package com.mm.nexttasks

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.mm.nexttasks.databinding.ActivityMainBinding
import com.mm.nexttasks.db.AppDatabase
import com.mm.nexttasks.db.dao.TaskListDao
import com.mm.nexttasks.ui.taskList.TaskListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private var _database: AppDatabase? = null
    private val database get() = _database!!
    private var taskListDao: TaskListDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.appBarMain.toolbar

        setSupportActionBar(toolbar)

        _database = MainApp.database!!
        taskListDao = database.taskListDao()

        binding.appBarMain.fab.setOnClickListener {
            val intent = Intent(this, TaskEditActivity::class.java)
            startActivity(intent)
        }


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val taskListsList = taskListDao!!.getAll()

        val allTaskListsMenuItem = navView.menu.add(Menu.FIRST, 0, Menu.NONE, getText(R.string.task_list_show_all))
        for (list in taskListsList) {
            navView.menu.add(Menu.FIRST, list.taskListId.toInt(), Menu.NONE, list.name)
        }
        navView.menu.setGroupCheckable(Menu.FIRST, true, true)
        allTaskListsMenuItem.setChecked(true)
        supportActionBar?.title = getText(R.string.task_list_show_all)

        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_content_main, TaskListFragment())
            .commit()

        // Set up the click listener for the navigation drawer items
        navView.setNavigationItemSelectedListener { menuItem ->
            // Get the selected item from the database
            val selectedItem = menuItem.title.toString()
            supportActionBar?.title = selectedItem
            val taskList = if (menuItem.itemId == allTaskListsMenuItem.itemId) {null} else selectedItem

            // Update the fragment with the selected item's information
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, TaskListFragment.newInstance(taskList))
                .commit()

            drawerLayout.close()
            true
        }

//        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        appBarConfiguration = AppBarConfiguration(setOf(
//                R.id.nav_home), drawerLayout)
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}