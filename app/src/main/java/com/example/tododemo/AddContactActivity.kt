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
    private var reqItemList: MutableList<FriendRequest> = mutableListOf()
    private var friendItemList: MutableList<Friends> = mutableListOf()
    private var listViewItems: ListView? = null
    lateinit var requestAdapter: FriendRequestAdapter
    lateinit var friendAdapter: FriendAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
        //updateUI(currentUser)

        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!
        listViewItems = findViewById<View>(R.id.lvFrdRequests) as ListView
        reqItemList = mutableListOf<FriendRequest>()
        requestAdapter = FriendRequestAdapter(this, reqItemList!!)
        friendItemList = mutableListOf<Friends>()
        friendAdapter = FriendAdapter(this, friendItemList)
        listViewItems!!.setAdapter(requestAdapter)
        db.orderByKey().addListenerForSingleValueEvent(itemListener)

        btnSendFriendRequest.setOnClickListener(View.OnClickListener {
            if (etFriendRequestEmail.text != null) {
                // seems fine for now
                addFriendRequest(currentUser)
                val okToast = Toast.makeText(applicationContext, "Friend request sent!", Toast.LENGTH_LONG)
                okToast.show()
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

    var itemListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get Post object and use the values to update the UI
            addDataToList(dataSnapshot)
        }
        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Item failed, log a message
            Log.w("AddContactActivity", "loadItem:onCancelled", databaseError.toException())
        }
    }

    private fun addDataToList(dataSnapshot: DataSnapshot) {
        val items = dataSnapshot.children.iterator()
        //Check if current database contains any collection
        if (items.hasNext()) {
            val toDoListindex = items.next()
            val itemsIterator = toDoListindex.children.iterator()

            //check if the collection has any items or not
            while (itemsIterator.hasNext()) {

                //get current item
                val currentItem = itemsIterator.next()
                val reqItem = FriendRequest.create()

                //get current data in a map
                val map = currentItem.getValue() as HashMap<String, Any>
                reqItem.objId = currentItem.key
                reqItem.accepted = map.get("accepted") as Boolean?
                reqItem.hopefulFriendEmail = map.get("hopefulFriendEmail") as String?
                reqItem.requesterEmail = map.get("requesterFriendEmail") as String?
                reqItemList!!.add(reqItem)
            }
        }
        //alert adapter that has changed
        requestAdapter.notifyDataSetChanged()
    }

    private fun initReqList() {
        // gets snapshot of DB data
        val reqListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(reqItemList) {
                    it.getValue<FriendRequest>(FriendRequest::class.java)
                }
                // passes reqItemList list into check function
                checkFriendRequests(reqItemList)

                dataSnapshot.children.mapNotNullTo(friendItemList) {
                    it.getValue<Friends>(Friends::class.java)
                }
                checkFriends(friendItemList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        db.child("").addValueEventListener(reqListener)
    }

    // adds friendRequest() type object to DB under "friendRequests"
    fun addFriendRequest(user: FirebaseUser) {
        val friend = FriendRequest.create()
        val currentUser = auth.currentUser
        friend.hopefulFriendEmail = etFriendRequestEmail.text.toString()
        friend.requesterEmail = user.email
        friend.accepted = false
        val tableId = db.child("friendRequests/").push().key
        friend.objId = tableId.toString()
        db.child("friendRequests/").child(tableId.toString()).setValue(friend)
    }

    // checks friendRequests if currentUser is located under hopefulFriend
    private fun checkFriendRequests(requests: MutableList<FriendRequest>) {
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        var userHasReq: MutableList<FriendRequest> = mutableListOf()
        var i = 0
        while(i < requests.count()) {
            if(requests[i].hopefulFriendEmail!! == currentUser!!.email) {
                userHasReq.add(requests[i])
            }
            i++
        }
        if (userHasReq.isNotEmpty())
            updateReqView(userHasReq)
    }

    //checks 'friends' if currentUser is located under friend1 or friend2
    private fun checkFriends(friends: MutableList<Friends>) {
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        var userHasFriend: MutableList<Friends> = mutableListOf()
        var u = 0
        while(u < friends.count()) {
            if(friends[u].friend1!! == currentUser!!.email) {
                userHasFriend.add(friends[u])
            }
            u++
        }
        if (userHasFriend.isNotEmpty())
            updateFriendView(userHasFriend)
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

    fun acceptFriend(user: FirebaseUser) {
        // need to implement this when user accepts friend request to add both users to each others friends and delete FriendRequest in question
        val friends = Friends.create()
        val currentUser = auth.currentUser
        // need to get the requesterEmail from current list item to friends.friend1

        friends.friend2 = currentUser!!.email
        val tableId = db.child("friends/").push().key
        friends.objectId = tableId.toString()
        db.child("friends/").child(tableId.toString()).setValue(friends)
    }

    fun declineFriend() {
        // same as above, but only deletes the friendRequst in question

    }
}
