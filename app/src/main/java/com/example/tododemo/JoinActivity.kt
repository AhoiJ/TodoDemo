package com.example.tododemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_join.*

class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var db: DatabaseReference

    private var toDo: MutableList<ToDoList> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        // using this now as a test to add other users to projects
        // Check if user is signed in (non-null) and update UI accordingly.

        // Start of test
        auth = FirebaseAuth.getInstance()
        // sets the database instance to current users path
        db = FirebaseDatabase.getInstance().getReference("todos/")

        initToDoList()


    }

    private fun modify(toDo: MutableList<ToDoList>){
        val currentUser = auth.currentUser
        var memberAsList: List<String> = mutableListOf()
        memberAsList += toDo[2].memberId!![0]
        memberAsList += currentUser!!.uid
        toDo[0].memberId = memberAsList

        val path = "memberId"

        db.child("-LtEtfS10QuwO10hQGF4").child(path).setValue(toDo[0].memberId)

    }

    private fun initToDoList() {
        // gets snapshot of DB data
        val todoListener = object : ValueEventListener {
            // refuses to work unless using mutableList<ToDoList>
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(toDo) {
                    it.getValue<ToDoList>(ToDoList::class.java)
                }
                // passes to-do list into check function
                modify(toDo)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        db.child("").addValueEventListener(todoListener)
    }

}
