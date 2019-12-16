package com.example.tododemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/*
 Some of code taken from https://demonuts.com/kotlin-listview-button/
         */

class FriendRequestAdapter(
    private val context: Context,
    private val friendReqItemList: MutableList<FriendRequest>
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
            convertView = inflater.inflate(R.layout.listview_friend_requests, null, true)

            holder.listViewInvite = convertView.findViewById(R.id.list_friend_invite) as TextView
            holder.btnAccept = convertView.findViewById(R.id.btnAccept) as Button
            holder.btnDecline = convertView.findViewById(R.id.btnDecline) as Button

            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        holder.listViewInvite!!.setText(AddContactActivity.friendRequestList.get(position).requesterEmail)

        // set tags for buttons
        holder.btnAccept!!.setTag(R.integer.btnAcceptView, convertView)
        holder.btnAccept!!.setTag(R.integer.btnAcceptPos, position)
        holder.btnDecline!!.setTag(R.integer.btnDeclineView, convertView)
        holder.btnDecline!!.setTag(R.integer.btnDeclinePos, position)

        // acceptButton onClickListener
        holder.btnAccept!!.setOnClickListener {
            // get position from button
            val pos = holder.btnAccept!!.getTag(R.integer.btnAcceptPos) as Int
            // initialize friendReq
            val friendReq: FriendRequest
            // get request using button position
            friendReq = AddContactActivity.friendRequestList.get(pos)
            // init list to hold friends
            var listOfFriends: MutableList<String> = mutableListOf()
            // updates list to the one in database
            listOfFriends.addAll(AddContactActivity.friendList)
            // add the new friend from request
            listOfFriends.add(friendReq.requesterEmail.toString())
            // init list that holds parsed list
            var parsedList: MutableList<FriendRequest>
            // parse list to contain what is seen on listView
            parsedList = parseList(AddContactActivity.friendRequestList)

            // Set friendRequest accepted value to true
            setAccepted(parsedList, pos)
            // push new list of friends
            db.child("contacts/").child(currentUser!!.uid).child("friends").setValue(parsedList)
            // clear local list to avoid bugs
            listOfFriends.clear()
        }

        // listener for decline button
        holder.btnDecline!!.setOnClickListener {
            // get position from button
            val pos = holder.btnDecline!!.getTag(R.integer.btnDeclinePos) as Int
            // init list that holds parsed list
            var parsedList: MutableList<FriendRequest>
            // parse list to contain what is seen on listView
            parsedList = parseList(AddContactActivity.friendRequestList)
            // remove the request from DB
            removeRequest(parsedList, pos)

        }

        return convertView!!

    }

    fun setAccepted(
        requestList: MutableList<FriendRequest>,
        position: Int
    ) {

        db = FirebaseDatabase.getInstance().reference
        // removes the object where request was stored from database
        db.child("friendRequests").child(requestList[position].objId.toString()).child("accepted")
            .setValue(true)

    }


    // function to remove accepted request from list
    fun removeRequest(
        requestList: MutableList<FriendRequest>,
        position: Int
    ) {

        db = FirebaseDatabase.getInstance().reference
        // removes the object where request was stored from database
        db.child("friendRequests").child(requestList[position].objId.toString())
            .removeValue()


    }

    fun parseList(requestList: MutableList<FriendRequest>): MutableList<FriendRequest> {
        val currentUser = auth.currentUser
        var userHasList: MutableList<FriendRequest> = mutableListOf()
        var i: Int = 0
        while (i < requestList.count()) {
            if (requestList[i].hopefulFriendEmail == currentUser!!.email.toString()) {
                userHasList.add(requestList[i])
            }
            i++
        }
        return userHasList
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

    private inner class ViewHolder {

        var listViewInvite: TextView? = null
        var btnAccept: Button? = null
        var btnDecline: Button? = null

    }

}