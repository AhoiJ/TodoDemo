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

        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!
        // the listview in which is the custom friendrequest xml (listview_friend_requests.xml)
        listViewItems = findViewById<View>(R.id.lvFrdRequests) as ListView
        // init an empty friendRequest list for displaying
        reqItemList = mutableListOf<FriendRequest>()
        requestAdapter = FriendRequestAdapter(this, reqItemList!!)
        // init an empty friend list for displaying
        friendItemList = mutableListOf<Friends>()
        friendAdapter = FriendAdapter(this, friendItemList)
        // set friendRequestAdapter for listViewItems
        listViewItems!!.setAdapter(requestAdapter)
        // listener that runs onDataChange once after itemListener is called
        db.orderByKey().addListenerForSingleValueEvent(itemListener)

        // onclicklistener for the "Send request" button
        btnSendFriendRequest.setOnClickListener(View.OnClickListener {
            if (etFriendRequestEmail.text != null) {
                sendFriendRequest(currentUser)
                val okToast = Toast.makeText(applicationContext, "Friend request sent!", Toast.LENGTH_LONG)
                okToast.show()
            }
            else {
                val toast = Toast.makeText(applicationContext, "Please enter an email", Toast.LENGTH_LONG)
                toast.show()
            }
        })
    }

    // listener for listview
    private var itemListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get Post object and use the values to update the UI
            addDataToList(dataSnapshot)
        }
        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Item failed, log a message
            Log.w("AddContactActivity", "loadItem:onCancelled", databaseError.toException())
        }
    }

    // fills the reqItemList object with FriendRequest type object using snapshots. Seems very oregano sandwich
    private fun addDataToList(dataSnapshot: DataSnapshot) {
        // iterator goes through the collection once
        val items = dataSnapshot.children.iterator()
        //Check if current database contains any collection
        if (items.hasNext()) {
            val reqListindex = items.next()
            val itemsIterator = reqListindex.children.iterator()

            //check if the collection has any items or not
            while (itemsIterator.hasNext()) {

                //next gets current item and moves iterator position forward
                val currentItem = itemsIterator.next()
                //make empty FriendRequest object
                val reqItem = FriendRequest.create()

                //get current data in a map
                val map = currentItem.getValue() as HashMap<String, Any>
                // takes them from the created map and puts them into the reqItem object (seems redundant but WHATEVER)
                reqItem.objId = currentItem.key
                reqItem.accepted = map.get("accepted") as Boolean?
                reqItem.hopefulFriendEmail = map.get("hopefulFriendEmail") as String?
                reqItem.requesterEmail = map.get("requesterFriendEmail") as String?
                // adds reqItem to the reqItemList object which goes to the adapter
                reqItemList!!.add(reqItem)
            }
        }
        //alert adapter that data has changed
        requestAdapter.notifyDataSetChanged()
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


}
