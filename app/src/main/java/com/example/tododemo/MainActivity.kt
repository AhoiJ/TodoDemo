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
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.tododemo.db.TaskContract
import com.example.tododemo.db.TaskDbHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.Timestamp
import java.time.Instant
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity()  {

    private lateinit var auth: FirebaseAuth
    lateinit var db: DatabaseReference

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        //  updateUI(currentUser) // need to implement UpdateUI that gets user data from firebase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        // initialize list to hold data that will be saved
        var listTodo: List<String> = mutableListOf()

        btnAddTodo.setOnClickListener(View.OnClickListener {
            if (etTodoItem.text != null) {
                val todo = etTodoItem.text.toString()
                listTodo += todo
                updateList(listTodo)
                etTodoItem.setText("")
            }
        })

        btnPublish.setOnClickListener(View.OnClickListener {
            // Checks is title empty and listTodo empty, if eighter emty, does not publish
            if (etTitle.text != null && listTodo.isNotEmpty()) {
                // Add title as Table name to database and add to-do items as items
                val createdTitle = etTitle.text.toString()
                // creates new to-do in firebase
                addTodo(createdTitle, listTodo)
                finish()

            } else
                Toast.makeText(applicationContext, "Please add Title or contents", Toast.LENGTH_SHORT).show()
        })
    }

    // updates the displayed list
    fun updateList(listTodo: List<String>) {
        val adapter = ArrayAdapter(
            this,
            R.layout.listview_item, listTodo
        )
        val listView: ListView = findViewById(R.id.todoList)
        listView.setAdapter(adapter)
    }

    fun addTodo(createdTitle: String, listTodo: List<String>){
        val list = ToDoList()
        // gets user id to input into list
        val currentUser = auth.currentUser
        list.title = createdTitle
        list.tasks = listTodo
        list.done = false
        list.startTime = Timestamp(System.currentTimeMillis()).toString()
        // user id as first table identification
        val key = currentUser!!.uid
        // get new 'key' for table id
        val tableId = db.child("todos/" + key).push().key
        db.child("todos/" + key).child(tableId.toString()).setValue(list)
    }

}
