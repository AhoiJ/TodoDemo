package com.example.tododemo

import android.content.ContentValues
import android.content.DialogInterface
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.tododemo.db.TaskContract
import com.example.tododemo.db.TaskDbHelper
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var  mHelper: TaskDbHelper
    private lateinit var mTaskListView: ListView
    private lateinit var mAdapter: ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mHelper = TaskDbHelper(this)
        mTaskListView = findViewById(R.id.list_todo)

        // testing connection to firebase
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")
        myRef.setValue("Hello, World!")


        updateUI()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun deleteTask(view: View){
        var parent = view.getParent() as View
        var taskTextView: TextView = parent.findViewById(R.id.task_title)
        var task: String = taskTextView.text.toString()
        var db = mHelper.writableDatabase
        db.delete(TaskContract.TABLE, TaskContract.COL_TASK_TITLE + " = ?", arrayOf<String>(task))
        db.close()
        updateUI()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_task -> {
                val taskEditText = EditText(this)
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Add a new task")
                    .setMessage("What do you want to do next?")
                    .setView(taskEditText)
                    .setPositiveButton("Add", object : DialogInterface.OnClickListener {

                        override fun onClick(dialog: DialogInterface, which: Int) {
                            val task = taskEditText.text.toString()
                            val db = mHelper.writableDatabase
                            val values = ContentValues()
                            values.put(TaskContract.COL_TASK_TITLE, task)
                            db.insertWithOnConflict(TaskContract.TABLE,
                                null,
                                values,
                                SQLiteDatabase.CONFLICT_REPLACE)
                            db.close()
                            updateUI() // may be misplaced
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create()
                dialog.show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI(){
        var taskList:ArrayList<String> = ArrayList()
        val db = mHelper.getReadableDatabase()
        var cursor = db.query(TaskContract.TABLE,
            arrayOf<String>( TaskContract.COL_TASK_TITLE), null, null, null, null, null)

        while(cursor.moveToNext()){
            var idx: Int = cursor.getColumnIndex(TaskContract.COL_TASK_TITLE)
            taskList.add(cursor.getString(idx))

        }

        if (!::mAdapter.isInitialized){

            mAdapter = ArrayAdapter(this, R.layout.item_todo,R.id.task_title,taskList)
            mTaskListView.adapter = mAdapter
        } else{
            mAdapter.clear()
            mAdapter.addAll(taskList)
            mAdapter.notifyDataSetChanged()
        }
        cursor.close()
        db.close()
    }

}
