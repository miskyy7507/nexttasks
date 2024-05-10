package com.mm.nexttasks

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

class TodoDatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME: String = "TodoList.db"
        const val DATABASE_VERSION: Int = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE Tasks (" +
                "taskID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "category TEXT, " +
                "priority TEXT, " +
                "done INTEGER, " +
                "cardColor INTEGER, " +
                "termTimestamp INTEGER" +
                ");")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Tasks;")
        this.onCreate(db)
    }

    fun addTask(task: TaskModel) {
        val db: SQLiteDatabase = this.writableDatabase
        val cv: ContentValues = ContentValues()

        cv.put("title", task.title)
        cv.put("category", task.category)
        cv.put("priority", task.priority)
        cv.put("done", if (task.isDone) 1 else 0)
        cv.put("cardColor", task.cardColor)
        cv.put("termTimestamp", task.termTimestamp)

        val result = db.insert("Tasks", null, cv)
        if (result == -1L) {
            //@TODO add error handling
        }
    }
}
