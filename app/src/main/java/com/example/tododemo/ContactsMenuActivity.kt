package com.example.tododemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_contacts_menu.*

class ContactsMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_menu)


        btnViewFriends.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, FriendsActivity::class.java)
            startActivity(intent)
        })


        btnViewFriendRequests.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, AddContactActivity::class.java)
            startActivity(intent)
        })

    }
}
