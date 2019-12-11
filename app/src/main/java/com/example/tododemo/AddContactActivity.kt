package com.example.tododemo

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add_contact.*


class AddContactActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var db: DatabaseReference
    private var reqItemList: MutableList<FriendRequest> = mutableListOf()
    private var listViewItems: ListView? = null
    lateinit var requestAdapter: FriendRequestAdapter


    public override fun onStart() {
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!
        // initialize listView for requests
        listViewItems = findViewById<View>(R.id.lvFrdRequests) as ListView
        // initialize adapter
        requestAdapter = FriendRequestAdapter(this, reqItemList)
        // set friendRequestAdapter for listViewItems
        listViewItems!!.setAdapter(requestAdapter)
        friendRequestList = mutableListOf<FriendRequest>()
        friendList = mutableListOf<String>()

        // updates friendList when user accepts request
        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        val ctListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                friendList.clear() // clear list so there are no local duplicates
                dataSnapshot.children.mapNotNullTo(AddContactActivity.friendList) {
                    it.getValue<String>(String::class.java)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }

        db.child("contacts").child(currentUser!!.uid).child("friends")
            .addValueEventListener(ctListener)


        // onClickListener for the "Send request" button
        btnSendFriendRequest.setOnClickListener(View.OnClickListener {
            if (etFriendRequestEmail.text != null) {
                var i = 0
                var isAlreadyFriend: Boolean = false
                while (i < friendList.count() || i == 0) {
                    if (friendList.count() == 0) {
                        if (friendRequestList.count() != 0) {
                            if (friendRequestList[i].hopefulFriendEmail == etFriendRequestEmail.text.toString())
                                isAlreadyFriend = true
                        }
                    } else if (friendList[i] == etFriendRequestEmail.text.toString() || friendRequestList[i].hopefulFriendEmail == etFriendRequestEmail.text.toString())
                        isAlreadyFriend = true
                    i++
                }
                if (isAlreadyFriend == false) {
                    sendFriendRequest(currentUser)
                    val okToast =
                        Toast.makeText(
                            applicationContext,
                            "Friend request sent!",
                            Toast.LENGTH_SHORT
                        )
                    okToast.show()
                } else {
                    val okToast =
                        Toast.makeText(
                            applicationContext,
                            "User is already your friend or has an unanswered request",
                            Toast.LENGTH_SHORT
                        )
                    okToast.show()
                }
            } else {
                val toast =
                    Toast.makeText(applicationContext, "Please enter an email", Toast.LENGTH_SHORT)
                toast.show()
            }
        })

        // gets snapshot of DB data
        val requestListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                friendRequestList.clear()
                dataSnapshot.children.mapNotNullTo(friendRequestList) {
                    it.getValue<FriendRequest>(FriendRequest::class.java)
                }
                // parses list to contain only those for this user
                userHasAccessToRequests(friendRequestList)
                addAcceptedRequests(friendRequestList)
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

    // checks if user is the receiver on list and collects a list for displaying
    private fun userHasAccessToRequests(lista: MutableList<FriendRequest>) {
        // get users instance
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        var userHasList: MutableList<FriendRequest> = mutableListOf()
        // counters for while statements
        var i = 0
        // while i is less than lista size
        if (!lista.isEmpty()) {
            while (i < lista.count()) {
                if (lista[i].hopefulFriendEmail == currentUser!!.email.toString()) {
                    if (lista[i].accepted == false)
                        userHasList.add(lista[i])
                }
                i++
            }
        }
        addRequestsToList(userHasList)
    }

    // fills the reqItemList object with FriendRequest type object using snapshots. Seems very oregano sandwich
    private fun addRequestsToList(reqItemList: MutableList<FriendRequest>) {
        val adapter = FriendRequestAdapter(this, reqItemList)
        val listView: ListView = findViewById(R.id.lvFrdRequests)
        listView.setAdapter(adapter)
    }

    // adds friendRequest() type object to DB under "friendRequests"
    fun sendFriendRequest(user: FirebaseUser) {
        // creates empty FriendRequest object
        val friend = FriendRequest.create()
        // fills the object with proper data
        friend.hopefulFriendEmail = etFriendRequestEmail.text.toString()
        friend.requesterEmail = user.email
        friend.accepted = false
        val tableId = db.child("friendRequests/").push().key
        //puts the tableId inside the object if it's used in the future for anything
        friend.objId = tableId.toString()
        // sets the filled FriendRequest-object to DB under friendRequests under the above-generated tableId
        db.child("friendRequests/").child(tableId.toString()).setValue(friend)
    }

    companion object {
        // lateinit var friendRequestList: ArrayList<FriendRequest>
        lateinit var friendRequestList: MutableList<FriendRequest>
        lateinit var friendList: MutableList<String>
    }

}
