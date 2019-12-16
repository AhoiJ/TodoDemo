package com.example.tododemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class JoinRequestAdapter(
    private val context: Context,
    private val singleRequest: MutableList<JoinRequest>
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
            convertView = inflater.inflate(R.layout.item_join, null, true)

            holder.requestTitle = convertView.findViewById(R.id.tvJoinHeader) as TextView
            holder.acceptRequest = convertView.findViewById(R.id.btnAcceptJoin) as Button
            holder.declineRequest = convertView.findViewById(R.id.btnDeclineJoin) as Button

            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser // null for some reason

        holder.requestTitle!!.setText(singleRequest[position].todoTitle)


        holder.acceptRequest!!.setTag(R.integer.btnAcceptView, convertView)
        holder.acceptRequest!!.setTag(R.integer.btnAcceptPos, position)
        holder.declineRequest!!.setTag(R.integer.btnDeclineView, convertView)
        holder.declineRequest!!.setTag(R.integer.btnDeclinePos, position)
        // onClickListener for accept button
        holder.acceptRequest!!.setOnClickListener {
            // get position from button, this may be error cause integer file is used in 2 adapters
            val pos = holder.acceptRequest!!.getTag(R.integer.btnAcceptPos) as Int
            // get request at position
            var temp = singleRequest.get(pos)
            // init list to hold all members
            var membersList: MutableList<String> = mutableListOf()
            // use requests objId to get members
            membersList.clear()
            membersList = getMembers(temp)
            var i = 0
            for (members in membersList) {
                var tempString = members
                var newString = tempString.replace("[", "")
                tempString = newString.replace("]", "")
                membersList[i] = tempString
                i++
            }
            membersList.add(currentUser!!.uid)
            // remove request from db
            removeRequest(singleRequest, pos)

            db.child("todos/").child(temp.todoObjId.toString()).child("memberId")
                .setValue(membersList)
            membersList.clear()
        }

        // onClickListener for decline button
        holder.declineRequest!!.setOnClickListener{
            val pos = holder.declineRequest!!.getTag(R.integer.btnDeclinePos) as Int

            removeRequest(singleRequest, pos)
        }


        return convertView!!
    }


    private fun getMembers(temp: JoinRequest): MutableList<String> {
        var membersList: MutableList<String> = mutableListOf()
        for (item in JoinActivity.toDoForCheck) {
            if (item.objId == temp.todoObjId) {
                membersList.add(item.memberId.toString())
            }

        }
        return membersList
    }

    // function to remove accepted request from list
    fun removeRequest(
        requestList: MutableList<JoinRequest>,
        position: Int
    ) {

        db = FirebaseDatabase.getInstance().reference
        // removes the object where request was stored from database
        db.child("joinRequests").child(requestList[position].todoObjId.toString())
            .removeValue()


    }



    override fun getItem(position: Int): Any {
        return singleRequest[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return singleRequest.count()
    }

    private inner class ViewHolder {

        var acceptRequest: Button? = null
        var declineRequest: Button? = null
        var requestTitle: TextView? = null

    }
}