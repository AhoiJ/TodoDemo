package com.example.tododemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


        btnSignUp.setOnClickListener(View.OnClickListener {

            signUp()
        })

        btnLogin.setOnClickListener(View.OnClickListener {


            login()
        })

    }


    fun signUp() {


    }

    fun login() {

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}
