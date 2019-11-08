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
        val currentUser = auth.currentUser
        updateUI(currentUser) // need to implement UpdateUI that gets user data from firebase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_open)

        // this is where the magic happens
      //  initToDoList()

    }

    private fun updateUI(currentUser: FirebaseUser?){
        val path = currentUser!!.uid
        db = FirebaseDatabase.getInstance().getReference("todos/" + path)

        initToDoList(path)
       // updateView()
    }

    private fun initToDoList(path: String) {
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
        db.child("").addValueEventListener(todoListener)
    }
    // correctly displays to-do list titles but otherwise useless
    private fun updateView(lista: MutableList<ToDoList>){
        // initialize list
        var listaus: List<ToDoList> = mutableListOf()
        // may make no sense anymore?
        for (i in 0..lista.lastIndex ){
            listaus = lista
        }
        // create listview using custom layout item
        listaus[0].title
        val adapter = ToDoAdapter(this, listaus)
        val listView: ListView = findViewById(R.id.listview_1)
        listView.setAdapter(adapter)
        
    }

}
