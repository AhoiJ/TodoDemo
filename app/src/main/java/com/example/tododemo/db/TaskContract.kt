package com.example.tododemo.db

import android.provider.BaseColumns

class TaskContract {
    companion object {
        val DB_NAME = "com.example.tododemo.db"
        val DB_VERSION: Int = 1
        val TABLE = "tasks"
        val COL_TASK_TITLE = "title"
    }
}