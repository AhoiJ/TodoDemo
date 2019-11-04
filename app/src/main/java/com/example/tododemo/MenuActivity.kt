package com.example.tododemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser!!
        updateUI(currentUser)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        auth = FirebaseAuth.getInstance()

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

        btnLogOut.setOnClickListener(View.OnClickListener {
            logout()
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
        val intent = Intent(this, AddContactActivity::class.java)
        startActivity(intent)
    }

    fun updateUI(user: FirebaseUser){
        tvUserName.setText(getString(R.string.user_email) +" " + user.email)

    }

    private fun logout(){
        auth.signOut()
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }
}
