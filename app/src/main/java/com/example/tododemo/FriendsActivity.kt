package com.example.tododemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FriendsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var db: DatabaseReference

    private var listViewFriends: ListView? = null
    lateinit var friendAdapter: FriendAdapter
    private var friends: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!

        // initialize listview for friends
        listViewFriends = findViewById(R.id.lvCurrentFrd) as ListView
        // initialize adapter for friends
        friendAdapter = FriendAdapter(this, friends)
        // init list for friendRequests


        val contactListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                friends.clear()
                dataSnapshot.children.mapNotNullTo(friends) {
                    it.getValue<String>(String::class.java)
                }

                addFriendsToList(friends)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }

        db.child("contacts").child(currentUser.uid).child("friends")
            .addListenerForSingleValueEvent(contactListener)

    }


    private fun addFriendsToList(friendList: List<String>) {
        friendAdapter = FriendAdapter(this, friendList)
        val listView: ListView = findViewById(R.id.lvCurrentFrd)
        listView.setAdapter(friendAdapter)
    }

}
