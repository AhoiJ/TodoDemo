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
    lateinit var friendAdapter: FriendAdapter
    private var listViewFriends: ListView? = null
    //  private var friendList: MutableList<Friends> = mutableListOf()
    private var friendList: MutableList<String> = mutableListOf()


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

        // initialize listview for friends
        listViewFriends = findViewById(R.id.lvCurrentFrd) as ListView
        // initialize adapter for friends
        friendAdapter = FriendAdapter(this, friendList)
        // init list for friendRequests
        friendRequestList = mutableListOf<FriendRequest>()


        // onClickListener for the "Send request" button
        btnSendFriendRequest.setOnClickListener(View.OnClickListener {
            if (etFriendRequestEmail.text != null) {
                sendFriendRequest(currentUser)
                val okToast =
                    Toast.makeText(applicationContext, "Friend request sent!", Toast.LENGTH_LONG)
                okToast.show()
            } else {
                val toast =
                    Toast.makeText(applicationContext, "Please enter an email", Toast.LENGTH_LONG)
                toast.show()
            }
        })

        val contactListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                friendList.clear()
                dataSnapshot.children.mapNotNullTo(friendList){
                    it.getValue<String>(String::class.java)
                }
                addFriendsToList(friendList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }

          db.child("contacts").child(currentUser.uid).child("friends").addValueEventListener(contactListener)

        // gets snapshot of DB data
        val requestListener = object : ValueEventListener {
            // refuses to work unless using mutableList<ToDoList>
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(friendRequestList) {
                    it.getValue<FriendRequest>(FriendRequest::class.java)
                }
                // passes to-do list into check function
                userHasAccessToRequests(friendRequestList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        db.child("friendRequests").addValueEventListener(requestListener)

    }

    private fun addFriendsToList(friendList: List<String>) {
        friendAdapter = FriendAdapter(this, friendList)
        val listView: ListView = findViewById(R.id.lvCurrentFrd)
        listView.setAdapter(friendAdapter)
    }


    // checks if user is the receiver on list and collects a list for displaying
    private fun userHasAccessToRequests(lista: MutableList<FriendRequest>) {
        // get users instance
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        // initialize list witch will have to-dos the user has access to
        var userHasList: MutableList<FriendRequest> = mutableListOf()
        // counters for while statements
        var i = 0
        // while i is less than lista size
        while (i < lista.count()) {
            // if lista[i] creatorId is same as current users id add that to-do to users list collection
            if (lista[i].hopefulFriendEmail == currentUser!!.email.toString()) {
                userHasList.add(lista[i])
            }
            i++
        }
        // if user had some lists, pass them on to be displayed
        if (userHasList.isNotEmpty())
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
    }

}
