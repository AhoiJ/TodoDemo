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
        // init for todos
        toDoForCheck = mutableListOf()

        // gets snapshot of DB data
        val todoListener = object : ValueEventListener {
            // refuses to work unless using mutableList<ToDoList>
            override fun onDataChange(dt: DataSnapshot) {
                dt.children.mapNotNullTo(toDoForCheck) {
                    it.getValue<ToDoList>(ToDoList::class.java)
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        db.child("todos/").addListenerForSingleValueEvent(todoListener)


        // gets joinRequestList from DB
        val joinListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
               joinRequests.clear() // clear list so there are no local duplicates

                var children = dataSnapshot.children
                getDtSnap(children)


            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        db.child("joinRequests")
            .addValueEventListener(joinListener)


    }

    private fun getDtSnap(dt : Iterable<DataSnapshot>){
        dt.forEach{
            it.children.mapNotNullTo(joinRequests){
                it.getValue<JoinRequest>(JoinRequest::class.java)
            }
        }
        accessCheck(joinRequests)
    }
    private fun accessCheck(requests: MutableList<JoinRequest>){
        // user auth
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!
        // init list to hold requests for this user
        var checkedRequests : MutableList<JoinRequest> = mutableListOf()

        for (item in requests){
            if(item.receiverEmail == currentUser.email){
                checkedRequests.add(item)
            }
        }
        addRequestsToList(checkedRequests)
    }
    private fun addRequestsToList(requests: MutableList<JoinRequest>){
        //initialize adapter
        joinReqAdapter = JoinRequestAdapter(this, requests)
        lvFrdRequests.adapter = joinReqAdapter
    }

    companion object {
        lateinit var joinRequests: MutableList<JoinRequest>
        lateinit var toDoForCheck: MutableList<ToDoList>
    }
    override fun finish() {
        super.finish()
        joinRequests.clear()
        toDoForCheck.clear()
    }
}
