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


class FriendRequestAdapter(private val context: Context, private val friendReqItemList: MutableList<FriendRequest>) : BaseAdapter() {

    private lateinit var auth: FirebaseAuth
    lateinit var db: DatabaseReference

    private val fInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    //private var friendReqList = friendReqItemList

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val reqView = fInflater.inflate(R.layout.listview_friend_requests, parent, false)

        val requestTextView = reqView.findViewById(R.id.list_friend_invite) as TextView
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val request = getItem(position) as FriendRequest
        if (currentUser!!.email == request.hopefulFriendEmail) {
            requestTextView.text = request.requesterEmail
        }
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
}