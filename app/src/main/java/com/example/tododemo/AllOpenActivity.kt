package com.example.tododemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
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
        //val currentUser = auth.currentUser
        //  updateUI(currentUser) // need to implement UpdateUI that gets user data from firebase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_open)

        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        val firebaseUser = auth.currentUser // currently useless, should be in onStart
        val userID = firebaseUser!!.uid
        // this is where the magic happens
        initToDoList()

    }


    private fun initToDoList() {
        // gets snapchot of DB data
        val todoListener = object : ValueEventListener {
            // refuses to work unless using mutableList<ToDoList>
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(toDo){
                    it.getValue<ToDoList>(ToDoList::class.java)
                }
                updateView(toDo)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        db.child("todos").addValueEventListener(todoListener)
    }
    // correctly displays to-do list titles but otherwise useless
    private fun updateView(lista: MutableList<ToDoList>){
        // initialize list
        var listaus: List<ToDoList> = mutableListOf() // can be made into List<ToDoList>, need to check
        // initialize variable of selfmade class ToDoList
        var too = ToDoList()
        // goes through all elements in lista, 'too' can only hold [1] object at a time
        for (i in 0..lista.lastIndex ){
            listaus = lista
            // transfer title from too to a mutable list(i know it makes no sense)
          //  listaus += too.title
        }
        // create listview using custom layout item
        listaus[0].title
        val adapter = ToDoAdapter(this, listaus)
        val listView: ListView = findViewById(R.id.listview_1)
        listView.setAdapter(adapter)
        
    }

}
