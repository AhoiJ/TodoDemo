package com.example.tododemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView


class ToDoAdapter(private val context: Context, private val dataSource: ArrayList<ToDoList>): BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val rowView = inflater.inflate(R.layout.item_todo, parent, false)

        val titleTextView = rowView.findViewById(R.id.task_title) as TextView
        val startTimeTextView = rowView.findViewById(R.id.task_start_time) as TextView


        val recipe = getItem(position) as ToDoList

        titleTextView.text = recipe.title
        startTimeTextView.text = recipe.startTime.toString()



        return rowView
    }
}