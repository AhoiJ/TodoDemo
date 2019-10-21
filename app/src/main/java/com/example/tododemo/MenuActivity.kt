package com.example.tododemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        btnCreate.setOnClickListener(View.OnClickListener {
            createNew()
        })

        btnAllOpen.setOnClickListener(View.OnClickListener {
            allOpen()
        })

        btnJoin.setOnClickListener(View.OnClickListener {
            joinTodo()
        })
        btnContacts.setOnClickListener(View.OnClickListener {
            openContacts()
        })
    }


    fun createNew(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun allOpen(){
        val intent = Intent(this, AllOpenActivity::class.java)
        startActivity(intent)
    }

    fun joinTodo(){
        val intent = Intent(this, JoinActivity::class.java)
        startActivity(intent)
    }

    fun openContacts(){

    }
}
