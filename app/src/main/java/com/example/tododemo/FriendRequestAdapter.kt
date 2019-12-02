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


class FriendRequestAdapter(private val context: Context, private val friendReqItemList: MutableList<FriendRequest>) : BaseAdapter() {

    private val fInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    //private var friendReqList = friendReqItemList

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

       // val objectId: String = friendReqList.get(position).objId as String
       // val requesterEmail: String? = friendReqList.get(position).requesterEmail
       // val hopefulFriendEmail: String? = friendReqList.get(position).hopefulFriendEmail
        // var accepted: Boolean? = friendReqList.get(position).accepted

        val reqView = fInflater.inflate(R.layout.listview_friend_requests, parent, false)

        val requestTextView = reqView.findViewById(R.id.list_friend_invite) as TextView

        val request = getItem(position) as FriendRequest

        requestTextView.text = request.requesterEmail

        return reqView
    }

    override fun getItem(position: Int): Any {
        return friendReqItemList[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getCount(): Int {
        return friendReqItemList.size
    }

    //private class ListRowHolder(row: View?) {
     //   val label: TextView = row!!.findViewById<TextView>(R.id.list_friend_invite) as TextView
    //}
}