package com.example.tododemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

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

            //val friendReq = AddContactActivity.friendRequestList.get(pos)

            val friendReq: FriendRequest
            friendReq = AddContactActivity.friendRequestList.get(pos)
            var test: List<String> = mutableListOf()
            test += friendReq.requesterEmail.toString()
            val friend = Friends()
            friend.friends = test

            db.child("contacts/").child(currentUser!!.uid).setValue(friend)


        }

        return convertView!!

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