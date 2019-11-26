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


class FriendRequestAdapter(context: Context, friendReqItemList: MutableList<FriendRequest>) : BaseAdapter() {

    private val fInflater: LayoutInflater = LayoutInflater.from(context)
    private var friendReqList = friendReqItemList

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val objectId: String = friendReqList.get(position).objId as String
        val requesterEmail: String = friendReqList.get(position).requesterEmail as String
        val hopefulFriendEmail: String = friendReqList.get(position).hopefulFriendEmail as String
        var accepted: Boolean = friendReqList.get(position).accepted as Boolean

        val view: View
        val vh: ListRowHolder
        if (convertView == null) {
            view = fInflater.inflate(R.layout.listview_friend_requests, parent, false)
            vh = ListRowHolder(view)
            view.tag= vh
        }
        else {
            view = convertView
            vh = view.tag as ListRowHolder
        }

        vh.label.text = requesterEmail

        return view
    }

    override fun getItem(index: Int): Any {
        return friendReqList.get(index)
    }
    override fun getItemId(index: Int): Long {
        return index.toLong()
    }
    override fun getCount(): Int {
        return friendReqList.size
    }

    private class ListRowHolder(row: View?) {
        val label: TextView = row!!.findViewById<TextView>(R.id.list_friend_invite) as TextView
    }
}