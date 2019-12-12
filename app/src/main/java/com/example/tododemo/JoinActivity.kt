package com.example.tododemo

import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_join.*

class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var db: DatabaseReference

    lateinit var joinReqAdapter: JoinRequestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!

        //init joinRequest List
        joinRequests = mutableListOf()
       // stringListReq = mutableListOf()



        // gets joinRequestList from DB
        val joinListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
              /*  joinRequests.clear() // clear list so there are no local duplicates
                dataSnapshot.children.mapNotNullTo(joinRequests) {
                    it.getValue<JoinRequest>(JoinRequest::class.java)
                }

               */
                var children = dataSnapshot.children
                getDtSnap(children)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        db.child("joinRequests").child("").child("")
            .addValueEventListener(joinListener)


    }

    private fun getDtSnap(dt : Iterable<DataSnapshot>){
        dt.forEach{
            it.children.mapNotNullTo(joinRequests){
                it.getValue<JoinRequest>(JoinRequest::class.java)
            }
        }
        addRequestsToList(joinRequests)
    }
    private fun addRequestsToList(requests: MutableList<JoinRequest>){
        //initialize adapter
        joinReqAdapter = JoinRequestAdapter(this, requests)
        lvFrdRequests.adapter = joinReqAdapter
    }

    companion object {
        lateinit var joinRequests: MutableList<JoinRequest>
       // lateinit var stringListReq: MutableList<String>
    }
}
