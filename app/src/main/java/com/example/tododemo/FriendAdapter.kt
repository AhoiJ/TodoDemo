package com.example.tododemo

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class FriendAdapter(private val context: Context, friendItemList: MutableList<Friends>) : BaseAdapter() {

    private lateinit var auth: FirebaseAuth
    lateinit var db: DatabaseReference
    private val fInflater: LayoutInflater = LayoutInflater.from(context)
    private var friendList = friendItemList

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val friendView = fInflater.inflate(R.layout.listview_friends, parent, false)

        val friendTextView = friendView.findViewById(R.id.list_friends) as TextView
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val friend = getItem(position) as Friends
        if (currentUser!!.email == friend.friend1) {
            friendTextView.text = friend.friend1
        }
        else if (currentUser!!.email == friend.friend2) {
            friendTextView.text = friend.friend2
        }

        return friendView
    }

    override fun getItem(position: Int): Any {
        return friendList[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getCount(): Int {
        return friendList.size
    }
}