package com.example.tododemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_single_todo.*
import java.io.Serializable

class SingleTodo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_todo)

        //hakee tiedot AllOpenActivitysta
        singleTodo = intent.extras!!.get("toDoList") as ToDoList

        //hakee titlen tekstimuodossa
        //textView.setText(singleTodo.title)
        //items_list.setText(singleTodo.tasks)
    }

    companion object {
        lateinit var singleTodo: ToDoList
    }
}
