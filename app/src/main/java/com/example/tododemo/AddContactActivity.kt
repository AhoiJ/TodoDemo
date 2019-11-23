package com.example.tododemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import com.google.android.gms.common.internal.Objects
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_contact.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.sql.Timestamp

class AddContactActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var db: DatabaseReference

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

    fun addFriend(user: FirebaseUser) {
        val friend = FriendRequest()
        val currentUser = auth.currentUser
        friend.hopefulFriendEmail = etFriendRequestEmail.text.toString()
        friend.requesterEmail = user.email
        friend.accepted = false
        val tableId = db.child("friendRequests/").push().key
        friend.objId = tableId.toString()

    }

    fun acceptFriend() {
        // need to implement this when user accepts friend request to add both users to each others friends and delete FriendRequest in question
        val currentUser = auth.currentUser
        //need to find correct friendRequest from DB with hopefulFriendEmail being current user
        // how will this wörke?????
        // db.child("friends").child(currentUser!!.uid).setValue(requesterEmail.text.toString())
    }

}
