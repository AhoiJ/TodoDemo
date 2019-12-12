package com.example.tododemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
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

            holder.requestObjId = convertView.findViewById(R.id.tvJoinHeader) as TextView
            holder.acceptRequest = convertView.findViewById(R.id.btnAcceptJoin) as Button
            holder.declineRequest =  convertView.findViewById(R.id.btnDeclineJoin) as Button

            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser // null for some reason

        holder.requestObjId!!.setText(singleRequest[position].todoTitle)


        holder.acceptRequest!!.setTag(R.integer.btnAcceptView, convertView)
        holder.acceptRequest!!.setTag(R.integer.btnAcceptPos, position)
        holder.declineRequest!!.setTag(R.integer.btnDeclineView, convertView)
        holder.declineRequest!!.setTag(R.integer.btnDeclinePos , position)



        return convertView!!
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
        var requestObjId: TextView? = null

    }
}