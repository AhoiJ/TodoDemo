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
    //private var friendList: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!

        // initialize listview for friends
        listViewFriends = findViewById(R.id.lvCurrentFrd) as ListView
        // init list for friends
        friendList = mutableListOf<String>()
        // init list for friendRequests
        friendRequestList = mutableListOf<FriendRequest>()
        // initialize adapter for friends
        friendAdapter = FriendAdapter(this, friendList)

        // gets list of friends
        val contactListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                friendList.clear()
                dataSnapshot.children.mapNotNullTo(friendList) {
                    it.getValue<String>(String::class.java)
                }

                addFriendsToList(friendList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }

        db.child("contacts").child(currentUser.uid).child("friends")
            .addValueEventListener(contactListener)

        // gets snapshot of DB data
        val requestListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                FriendsActivity.friendRequestList.clear()
                dataSnapshot.children.mapNotNullTo(friendRequestList) {
                    it.getValue<FriendRequest>(FriendRequest::class.java)
                }
                // add accepted requests to friends
                addAcceptedRequests(FriendsActivity.friendRequestList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        db.child("friendRequests").addValueEventListener(requestListener)



    }

    private fun addAcceptedRequests(lista: MutableList<FriendRequest>) {
        // get users instance
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        var userHasList: MutableList<FriendRequest> = mutableListOf()
        // counters for while statements
        var i = 0
        var friendEmailAsList: MutableList<String?> = mutableListOf()
        if (!lista.isEmpty()) {
            while (i < lista.count()) {
                if (lista[i].requesterEmail == currentUser!!.email.toString()) {
                    if (lista[i].accepted == true) {
                        friendEmailAsList!!.add(lista[i].hopefulFriendEmail)
                        db.child("contacts/").child(currentUser!!.uid).child("friends")
                            .setValue(friendEmailAsList) // this needs to be a list
                        db.child("friendRequests").child(lista[i].objId.toString())
                            .removeValue()
                    }
                }
                i++
            }
        }
    }

    private fun addFriendsToList(friendList: List<String>) {
        friendAdapter = FriendAdapter(this, friendList)
        val listView: ListView = findViewById(R.id.lvCurrentFrd)
        listView.setAdapter(friendAdapter)
    }

    companion object {
        lateinit var friendList: MutableList<String>
        lateinit var friendRequestList: MutableList<FriendRequest>
    }

    override fun finish() {
        super.finish()
        friendList.clear()
        friendRequestList.clear()
    }

}

