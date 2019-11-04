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

    private var toDo: MutableList<ToDoList> = mutableListOf()

    private lateinit var auth: FirebaseAuth
    lateinit var _db: DatabaseReference

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        //val currentUser = auth.currentUser
        //  updateUI(currentUser) // need to implement UpdateUI that gets user data from firebase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_open)

        _db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        val firebaseUser = auth.currentUser // currently useless, should be in onStart
        val userID = firebaseUser!!.uid
        initToDoList()



    }


    private fun initToDoList() {
        val todoListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(toDo){
                    it.getValue<ToDoList>(ToDoList::class.java)
                }
                updateNakyma(toDo)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        _db.child("ToDos").addValueEventListener(todoListener)
    }

    private fun updateNakyma(lista: MutableList<ToDoList>){
       // tvTitle.setText(lista.title)
        var listaus: List<String?> = mutableListOf()

        var too = ToDoList()

        for (i in 0..lista.lastIndex ){
            too = lista[i]
            val titteli: String? = too.title

            listaus += titteli
        }




        val adapter = ArrayAdapter<String>(this, R.layout.listview_item, listaus)
        val listView: ListView = findViewById(R.id.listview_1)
        listView.setAdapter(adapter)


/*
        val listView: ListView = findViewById(R.id.listview_1)
        listView.setAdapter(adapter)

         */
    }

}


/*
    val adapter = ArrayAdapter(this,
            R.layout.listview_item, array)

        val listView: ListView = findViewById(R.id.listview_1)
        listView.setAdapter(adapter)

 */