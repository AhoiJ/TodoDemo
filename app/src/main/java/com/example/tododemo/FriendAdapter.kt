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


class FriendAdapter(private val context: Context, friendItemList: List<String?>) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var friendList = friendItemList

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val rowView = inflater.inflate(R.layout.listview_friends, parent, false)

        val friendEmailView = rowView.findViewById(R.id.list_friends) as TextView

        val friend = getItem(position) as String

        friendEmailView.text = friend

        return rowView
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

}