package com.example.tododemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
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
        //updateUI(currentUser)

        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        btnSendFriendRequest.setOnClickListener(View.OnClickListener {
            if (etFriendRequestEmail.text != null) {
                addFriend()
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

    fun addFriend() {
        val friend = FriendRequest()
        val currentUser = auth.currentUser
        // how to refer to current users email?
        // friend.requesterEmail = currentUser.email
        friend.accepted = false
        val tableId = db.child("friends/").push().key
        friend.objId = tableId.toString()

    }
}
