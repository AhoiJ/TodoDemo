package com.example.tododemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_single_todo.*
import java.io.Serializable

class SingleTodo : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var db: DatabaseReference

    lateinit var todoAdapter: SingleTodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_todo)


        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!

        //hakee tiedot AllOpenActivitysta
        singleTodo = intent.extras!!.get("toDoList") as ToDoList
        populateList(singleTodo)
        //hakee titlen tekstimuodossa
        textTitle.setText(singleTodo.title)
        // initialize friendList
        var friendList: MutableList<String> = mutableListOf()
        // initialize spinner
        var friendSpinner: Spinner = findViewById(R.id.friendSpinner)
        var spinnerArrayAdapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, friendList)


        // updates friendList
        val ctListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                friendList.clear() // clear list so there are no local duplicates
                dataSnapshot.children.mapNotNullTo(friendList) {
                    it.getValue<String>(String::class.java)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }

        db.child("contacts").child(currentUser!!.uid).child("friends")
            .addValueEventListener(ctListener)

    }

    override fun finish() {
        super.finish()
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
    }

    private fun populateList(singleTodo: ToDoList) {
        todoAdapter = SingleTodoAdapter(this, singleTodo)
        val listView: ListView = findViewById(R.id.items_list)
        listView.setAdapter(todoAdapter)
    }

    companion object {
        lateinit var singleTodo: ToDoList
    }

}
