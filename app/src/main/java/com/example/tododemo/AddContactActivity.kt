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
import kotlinx.android.synthetic.main.listview_friends.*
import java.sql.Timestamp

class AddContactActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var db: DatabaseReference
    private var toDo: MutableList<FriendRequest> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
        val currentUser = auth.currentUser!!
        // updateUI(currentUser)

        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
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

    private fun checkFriendRequests(requests: MutableList<FriendRequest>) {
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        var i = 0
        var u = 0
        var userHasList: MutableList<FriendRequest> = mutableListOf()
        while(i < requests.count()) {
            if(requests[i].hopefulFriendEmail!! == currentUser!!.email) {
                userHasList.add(requests[i])
            }
            i++
        }
        if (userHasList.isNotEmpty())
            updateView(userHasList)
    }

    // Updates listview with items when change occurs
    private fun updateView(lista: MutableList<FriendRequest>) {

        // pass list into custom adapter
        val adapter = FriendAdapter(this, lista)
        val listView: ListView = findViewById(R.id.lvFrdRequests)
        listView.setAdapter(adapter)

    }

    fun acceptFriend() {
        // need to implement this when user accepts friend request to add both users to each others friends and delete FriendRequest in question
        val currentUser = auth.currentUser
        //need to find correct friendRequest from DB with hopefulFriendEmail being current user
        //db.child("friends/").child(currentUser!!.uid).setValue(requesterEmail)
    }

}
