package com.example.tododemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_all_open.*

class AllOpenActivity : AppCompatActivity() {

    // initialize list to take data from db
    private var toDo: MutableList<ToDoList> = mutableListOf()

    private lateinit var auth: FirebaseAuth
    lateinit var db: DatabaseReference

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        auth = FirebaseAuth.getInstance()
        // sets the database instance to current users path
        db = FirebaseDatabase.getInstance().getReference("todos/")
        val currentUser = auth.currentUser
        updateUI(currentUser) // need to implement UpdateUI that gets user data from firebase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_open)

        // OnCreate currently needed only to start activity
        // used when implementing opening to-dos activity

    }
    // may get other uses later, dont delete yet
    private fun updateUI(currentUser: FirebaseUser?) {

        //  initToDo loads snapshot every time database for this user updates
        initToDoList()
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
                userHasAccess(toDo)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        db.child("").addValueEventListener(todoListener)
    }

    // Updates listview with items when change occurs
    private fun updateView(lista: MutableList<ToDoList>) {


        // pass list into custom adapter
        val adapter = ToDoAdapter(this, lista)
        val listView: ListView = findViewById(R.id.listview_1)
        listView.setAdapter(adapter)

    }

    // checks if user is on a to-dos member list and collects a list for displaying
    private fun userHasAccess(lista: MutableList<ToDoList>) {
        // get users instance
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        // initialize list witch will have to-dos the user has access to
        var userHasList: MutableList<ToDoList> = mutableListOf()
        // counters for while statements
        var i = 0
        var u = 0
        // while i is less than lista size
        while (i < lista.count()) {
            // if lista[i] creatorId is same as current users id add that to-do to users list collection
            if (lista[i].creatorId == currentUser!!.uid)
                userHasList.add(lista[i])
            // if user is not to-dos creator, user may still be a member
            else
            // makes sure we dont access memberId at a position that does not exist
                while (u < lista[i].memberId!!.count()) {
                    // if lista[i] memberId contains current users Id, add that to-do to users list collection
                    if (lista[i].memberId!![u] == currentUser.uid)
                        userHasList.add(lista[i])
                    u++
                }
            i++
        }
        // if user had some lists, pass them on to be displayed
        if (userHasList.isNotEmpty())
            updateView(userHasList)
    }

}
