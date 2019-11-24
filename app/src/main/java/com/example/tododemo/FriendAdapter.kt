package com.example.tododemo

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase


class FriendAdapter(context: Context, friendItemList: MutableList<Friends>) : BaseAdapter() {

    private val fInflater: LayoutInflater = LayoutInflater.from(context)
    private var friendList = friendItemList

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val objectId: String = friendList.get(position).objectId as String
        val friendName: String = friendList.get(position).friendName as String

        val view: View
        val vh: ListRowHolder
        if (convertView == null) {
            view = fInflater.inflate(R.layout.listview_friends, parent, false)
            vh = ListRowHolder(view)
            view.tag= vh
        }
        else {
            view = convertView
            vh = view.tag as ListRowHolder
        }

        vh.label.text = friendName

        return view
    }

    override fun getItem(index: Int): Any {
        return friendList.get(index)
    }
    override fun getItemId(index: Int): Long {
        return index.toLong()
    }
    override fun getCount(): Int {
        return friendList.size
    }

    private class ListRowHolder(row: View?) {
        val label: TextView = row!!.findViewById<TextView>(R.id.list_friends) as TextView
    }
}