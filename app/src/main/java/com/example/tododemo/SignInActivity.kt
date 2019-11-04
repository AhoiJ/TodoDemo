package com.example.tododemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and bypass login if still logged in
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        // initialize firebase auth
        auth = FirebaseAuth.getInstance()

        btnSignUp.setOnClickListener(View.OnClickListener {
            // checks if both fields are filled
            if (etEmailAddressSign.text.isNotEmpty() && etPasswordSign.text.isNotEmpty()) {
                //  val email = etEmailAddressSign.get
                // takes user input and passes them to signup function
                val email = etEmailAddressSign.text.toString()
                val pass = etPasswordSign.text.toString().trim()
                signUp(email, pass)
            } else
                Toast.makeText(
                    applicationContext,
                    "All Sign-Up fields are not filled",
                    Toast.LENGTH_SHORT
                ).show()
        })

        btnLogin.setOnClickListener(View.OnClickListener {
            // checks if both fields are filled
            if (etEmailAddressLogin.text.isNotEmpty() && etPasswordLogin.text.isNotEmpty()) {
                val email = etEmailAddressLogin.text.toString()
                val pass = etPasswordLogin.text.toString()
                login(email, pass)
            } else
                Toast.makeText(
                    applicationContext,
                    "All Login fields are not filled",
                    Toast.LENGTH_SHORT
                ).show()
        })

    }


    private fun signUp(email: String, pass: String) {
        // Firebase Authentication to create user with email and pass
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (!it.isSuccessful) // if creation failed show exception
                    Log.e("SignIn", "onComplete: Failed=" + it.getException())

                // else if successful show in success in log
                else
                    Log.d("SignIn", "Successfully created user with uid: ${it.result!!.user!!.uid}")
            }
            .addOnFailureListener {
                Log.d("SignIn", "Failed to create user: ${it.message}")
                Toast.makeText(
                    applicationContext,
                    "Failed to create user: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }

    private fun login(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Login", "signInWithEmail:success")
                    val user = auth.currentUser
                    val intent = Intent(this, MenuActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Login", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()

                }


            }

    }
}
