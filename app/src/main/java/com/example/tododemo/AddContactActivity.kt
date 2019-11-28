package com.example.tododemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ListView
import android.widget.Toast
import com.google.android.gms.common.internal.Objects
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add_contact.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.listview_friend_requests.*
import kotlinx.android.synthetic.main.listview_friends.*
import java.sql.Timestamp

class AddContactActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var db: DatabaseReference
    private var toDo: MutableList<FriendRequest> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
        // updateUI(currentUser)

        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!

        btnSendFriendRequest.setOnClickListener(View.OnClickListener {
            if (etFriendRequestEmail.text != null) {
                // not tested, need to check firebase
                addFriend(currentUser)
            }
            else {
                val toast = Toast.makeText(applicationContext, "Please enter an email", Toast.LENGTH_LONG)
                toast.show()
            }
        })

        // these 2 cause run errors if not commented out before working
        //btnAccept.setOnClickListener(View.OnClickListener {
          // acceptFriend()
        //})

        //btnDecline.setOnClickListener(View.OnClickListener {
           // declineFriend()
        //})
    }

    private fun initToDoList() {
        // gets snapshot of DB data
        val todoListener = object : ValueEventListener {
            // refuses to work unless using mutableList<ToDoList>
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(toDo) {
                    it.getValue<FriendRequest>(FriendRequest::class.java)
                }
                // passes to-do list into check function
                checkFriendRequests(toDo)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        db.child("").addValueEventListener(todoListener)
    }

    // adds friendRequest() type object to DB under "friendRequests"
    fun addFriend(user: FirebaseUser) {
        val friend = FriendRequest()
        val currentUser = auth.currentUser
        friend.hopefulFriendEmail = etFriendRequestEmail.text.toString()
        friend.requesterEmail = user.email
        friend.accepted = false
        val tableId = db.child("friendRequests/").push().key
        friend.objId = tableId.toString()
        db.child("friendRequests/").setValue(friend)
    }

    // checks friendRequests if currentUser is located under hopefulFriend
    private fun checkFriendRequests(requests: MutableList<FriendRequest>) {
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        var i = 0
        var userHasReq: MutableList<FriendRequest> = mutableListOf()
        while(i < requests.count()) {
            if(requests[i].hopefulFriendEmail!! == currentUser!!.email) {
                userHasReq.add(requests[i])
            }
            i++
        }
        if (userHasReq.isNotEmpty())
            updateReqView(userHasReq)
    }

    // same as above but with current friends (NEEDS CONFIRMATION WHERE FRIENDS ARE STORED, check fun acceptFriends)
    private fun checkFriends(friends: MutableList<Friends>) {
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        var u = 0
    }
    // Updates listview with items when change occurs
    private fun updateReqView(reqList: MutableList<FriendRequest>) {

        // pass list into custom adapter
        val requestAdapter = FriendRequestAdapter(this, reqList)
        val listView: ListView = findViewById(R.id.lvFrdRequests)
        listView.setAdapter(requestAdapter)

    }

    private fun updateFriendView(frdList: MutableList<Friends>) {
        // pass list into custom adapter
        val friendAdapter = FriendAdapter(this, frdList)
        val listView: ListView = findViewById(R.id.lvCurrentFrd)
        listView.setAdapter(friendAdapter)
    }

    fun acceptFriend() {
        // need to implement this when user accepts friend request to add both users to each others friends and delete FriendRequest in question
        val currentUser = auth.currentUser

        //db.child("friends/").child(currentUser!!.uid).setValue(requesterEmail)
    }

    fun declineFriend() {
        // same as above, but only deletes the friendRequst in question
    }
}
