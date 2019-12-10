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
    private val SingleItemList: ToDoList
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

        holder.subTask!!.setText(SingleTodo.singleTodo.tasks!![position])

        if (SingleTodo.singleTodo.taskDone!!.get(position) == 1)
            holder.taskDoneDid!!.isChecked = true
        else
            holder.taskDoneDid!!.isChecked = false

        holder.taskDoneDid!!.setTag(R.integer.taskDoneDidView, convertView)
        holder.taskDoneDid!!.setTag(R.integer.taskDoneDidPos, position)

        // acceptButton onClickListener
        holder.taskDoneDid!!.setOnClickListener {
            // get position from button
            val pos = holder.taskDoneDid!!.getTag(R.integer.taskDoneDidPos) as Int

            if(SingleTodo.singleTodo.taskDone!!.get(pos) == 1){
                SingleTodo.singleTodo.taskDone
            } else
                SingleTodo.singleTodo.taskDone!![pos] = 1

            db.child("todos").child(SingleTodo.singleTodo.objId.toString()).setValue(SingleTodo.singleTodo)
        }

        return convertView!!
    }

    override fun getItem(position: Int): Any {
        return SingleItemList.tasks!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return SingleItemList.tasks!!.count()
    }

    private inner class ViewHolder {

        var taskDoneDid: CheckBox? = null
        var subTask: TextView? = null
        //tarvitaan jos poisto ominaisuus lisätään
        //var deleteTask: ImageButton? = null

    }
}