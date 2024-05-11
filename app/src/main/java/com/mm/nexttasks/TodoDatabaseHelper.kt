package com.mm.nexttasks

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TodoDatabaseHelper(private val context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME: String = "TodoList.db"
        const val DATABASE_VERSION: Int = 1

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE Tasks (" +
                "taskID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "done INTEGER, " +
                "category TEXT, " +
                "priority TEXT, " +
                "cardColor INTEGER, " +
                "termTimestamp INTEGER" +
                ");")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Tasks;")
        this.onCreate(db)
    }

    fun addTask(task: TaskModel): Long {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put("title", task.title)
        cv.put("category", task.category)
        cv.put("priority", task.priority)
        cv.put("done", if (task.isDone) 1 else 0)
        cv.put("cardColor", task.cardColor)
        cv.put("termTimestamp", task.termTimestamp)

        val result = db.insert("Tasks", null, cv)

        return result
    }

    fun readAllData(): Cursor {
        val readQuery = "SELECT * FROM Tasks"
        val db: SQLiteDatabase = this.writableDatabase

        val cursor: Cursor = db.rawQuery(readQuery, null)

        return cursor
    }
}
