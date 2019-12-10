package com.example.tododemo

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class FriendAdapter(private val context: Context, friendItemList: List<String?>) : BaseAdapter() {

    //private val inflater: LayoutInflater =
        //context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private var friendList = friendItemList
    // private var friendList: MutableList<String> = mutableListOf()
    private lateinit var auth: FirebaseAuth
    lateinit var db: DatabaseReference

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val holder: ViewHolder
        //val rowView = inflater.inflate(R.layout.listview_friends, parent, false)

        if (convertView == null) {
            holder = ViewHolder()

            val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.listview_friends, null, true)

            holder.listViewFriend = convertView.findViewById(R.id.list_friends) as TextView
            holder.btnDeleteFriend = convertView.findViewById(R.id.btnDeleteFriend) as Button

            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser // null for some reason
        friendList = mutableListOf<String>()


        holder.listViewFriend!!.setText(FriendsActivity.friendList.get(position))

        // set tag for button
        holder.btnDeleteFriend!!.setTag(R.integer.btnDeleteView, convertView)
        holder.btnDeleteFriend!!.setTag(R.integer.btnDeletePos, position)

        // listener for remove button
        holder.btnDeleteFriend!!.setOnClickListener {
            // get position from button
            val pos = holder.btnDeleteFriend!!.getTag(R.integer.btnDeletePos) as Int
            // init list to hold friends
            var listOfFriends: MutableList<String> = mutableListOf()
            // updates list to the one in database
            listOfFriends.addAll(FriendsActivity.friendList)
            //remove friend from the local friend list
            listOfFriends.removeAt(pos)
            // push new list of friends
            db.child("contacts/").child(currentUser!!.uid).child("friends").setValue(listOfFriends)
            // clear local list to avoid bugs
            listOfFriends.clear()
        }



        //val friendEmailView = rowView.findViewById(R.id.list_friends) as TextView

        //val friend = getItem(position) as String

        //friendEmailView.text = friend

        return convertView!!
    }


    override fun getItem(position: Int): String? {
        return friendList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return friendList!!.size
    }

    private inner class ViewHolder {

        var listViewFriend: TextView? = null
        var btnDeleteFriend: Button? = null

    }
}