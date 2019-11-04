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
    lateinit var _db: DatabaseReference

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        //  updateUI(currentUser) // need to implement UpdateUI that gets user data from firebase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

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
            if (etTitle.text != null) {
                // Add title as Table name to database and add todo items as items
                val createdTitle = etTitle.text.toString()
                // creates new todo in firebase
                addTodo(createdTitle, listTodo)

            } else
                Toast.makeText(applicationContext, "Please add Title", Toast.LENGTH_SHORT).show()
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
        list.title = createdTitle
        list.tasks = listTodo
        list.done = false
        list.startTime = Timestamp(System.currentTimeMillis()).toString()
        val key = _db.child("ToDos").push().key
        list.objId = key
        _db.child("ToDos").child(key.toString()).setValue(list)
    }

}
