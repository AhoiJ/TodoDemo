package com.example.tododemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.core.view.isNotEmpty
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_single_todo.*
import java.io.Serializable

class SingleTodo : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var db: DatabaseReference

    lateinit var todoAdapter: SingleTodoAdapter

    override fun onStart() {
        super.onStart()

    }

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
        friendList = mutableListOf()


        // listener for spinner
        friendSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(
                    applicationContext,
                    "fail",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (friendSpinner.isNotEmpty()) {
                    if (parent.getItemAtPosition(position).equals("Select a friend to invite")) {
                        // do nothing, this is default value
                    } else {
                        var selected = parent.getItemAtPosition(position).toString()

                        db.child("joinRequests").child(singleTodo.objId.toString()).setValue(selected)
                    }
                }
            }
        }

        // updates friendList
        val ctListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                friendList.clear() // clear list so there are no local duplicates
                dataSnapshot.children.mapNotNullTo(friendList) {
                    it.getValue<String>(String::class.java)
                }
                initSpinner(friendList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }

        db.child("contacts").child(currentUser!!.uid).child("friends")
            .addListenerForSingleValueEvent(ctListener)

    }

    override fun finish() {
        super.finish()
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
    }

    private fun initSpinner(friendList: MutableList<String>) {
        // add a default text as first item
        friendList.add(0, "Select a friend to invite")
        // initialize spinner
        var spinnerArrayAdapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, friendList)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        friendSpinner.adapter = spinnerArrayAdapter
    }

    private fun populateList(singleTodo: ToDoList) {
        todoAdapter = SingleTodoAdapter(this, singleTodo)
        val listView: ListView = findViewById(R.id.items_list)
        listView.setAdapter(todoAdapter)
    }

    companion object {
        lateinit var singleTodo: ToDoList
        lateinit var friendList: MutableList<String>
        lateinit var joinRequestList: MutableList<String>
    }

}
