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


    private val fInflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

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
        val currentUser = auth.currentUser // null for some reason

        holder.listViewInvite!!.setText(AddContactActivity.friendRequestList.get(position).requesterEmail)

        holder.btnAccept!!.setTag(R.integer.btnAcceptView, convertView)
        holder.btnAccept!!.setTag(R.integer.btnAcceptPos, position)

        holder.btnAccept!!.setOnClickListener {
            val pos = holder.btnAccept!!.getTag(R.integer.btnAcceptPos) as Int

            val friendReq: FriendRequest
            // get request from button pos
            friendReq = AddContactActivity.friendRequestList.get(pos)
            var test: MutableList<String> = mutableListOf()
       //     test = getFriendsFromDatabase()

                test.addAll(AddContactActivity.friendList)
                test.add(friendReq.requesterEmail.toString())

                db.child("contacts/").child(currentUser!!.uid).child("friends").setValue(test)
                test.clear()
        }

        return convertView!!

    }
/*
    fun getFriendsFromDatabase(): MutableList<String> {
        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!
        val ctListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(friendList) {
                    it.getValue<String>(String::class.java)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }

        db.child("contacts").child(currentUser.uid).child("friends")
            .addListenerForSingleValueEvent(ctListener)

        return friendList
    }

 */

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