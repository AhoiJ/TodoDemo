package com.example.tododemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_single_todo.*
import java.io.Serializable

class SingleTodo : AppCompatActivity() {

    lateinit var todoAdapter: SingleTodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_todo)

        //hakee tiedot AllOpenActivitysta
        singleTodo = intent.extras!!.get("toDoList") as ToDoList
        populateList(singleTodo)
        //hakee titlen tekstimuodossa
        //textView.setText(singleTodo.title)
        //items_list.setText(singleTodo.tasks)
    }

    override fun finish() {
        super.finish()
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
    }

    private fun populateList(singleTodo: ToDoList) {
        todoAdapter = SingleTodoAdapter(this, singleTodo)
        val listView: ListView = findViewById(R.id.items_list)
        listView.setAdapter(todoAdapter)
    }

    companion object {
        lateinit var singleTodo: ToDoList
    }

}
