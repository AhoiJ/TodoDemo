package com.example.tododemo

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView

import android.widget.*

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/*
 Some of code taken from https://demonuts.com/kotlin-listview-button/
         */

class SingleTodoAdapter(
    private val context: Context,
    private val SingleItemList: MutableList<ToDoList>
) : BaseAdapter() {


    private lateinit var auth: FirebaseAuth
    lateinit var db: DatabaseReference
    //   private var friendList: MutableList<String> = mutableListOf()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            holder = ViewHolder()

            val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.activity_single_todo_adapter, null, true)

            holder.taskDoneDid = convertView.findViewById(R.id.task_done_did) as CheckBox
            holder.subTask = convertView.findViewById(R.id.sub_task) as TextView
            //holder.deleteTask = convertView.findViewById(R.id.delete_task) as ImageButton

            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser // null for some reason

        holder.subTask!!.setText(SingleTodo.singleTodo.get(position).title)

        return convertView!!
    }

    override fun getItem(position: Int): Any {
        return SingleItemList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return SingleItemList.size
    }

    private inner class ViewHolder {

        var taskDoneDid: CheckBox? = null
        var subTask: TextView? = null
        //var deleteTask: ImageButton? = null

    }
}