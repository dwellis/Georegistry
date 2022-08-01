package com.cognizant.checkin.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.cognizant.checkin.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    // flag for redirecting if admin
    private var adminFlag : Boolean = false

    // references for db
    private lateinit var database: DatabaseReference
    private lateinit var accounts : DatabaseReference
    private lateinit var account : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth
        // get db references for later access

        database = Firebase.database.reference
        accounts = Firebase.database.reference.child("accounts")
        account = accounts.child(Firebase.auth.currentUser?.uid.toString())

        // add a listener to the accounts list
        account.addValueEventListener(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.S)
            override fun onDataChange(snapshot: DataSnapshot) {
                if(auth.currentUser != null) {
                    // set admin flag
                    adminFlag = snapshot.child("admin").value.toString().toBoolean()

                    // checks for landing page option
                    if(adminFlag) {
                        val intent = Intent(applicationContext, RegistrarLanding::class.java)
                        startActivity(intent)
                    } else {
                        val intent = Intent(applicationContext, UserLanding::class.java)
                        startActivity(intent)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        // set button clicks for activity
        binding.login.setOnClickListener {

                signIn(binding.username.text.toString(), binding.password.text.toString())

        }

        binding.loginButtonCreateAccount.setOnClickListener {
            val intent = Intent(this, CreateAccount::class.java)
            startActivity(intent)
        }
    }

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    Toast.makeText(baseContext, "Signed In.", Toast.LENGTH_SHORT).show()

                    sendEmailVerification()

                    // get account to check admin flag
                    account = Firebase.database.reference.child("accounts").child(Firebase.auth.currentUser?.uid.toString())

                    account.addValueEventListener(object : ValueEventListener {
                        @RequiresApi(Build.VERSION_CODES.S)
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.child("admin").value.toString().toBoolean()) {
                                val intent = Intent(applicationContext, RegistrarLanding::class.java)
                                startActivity(intent)
                            } else {
                                val intent = Intent(applicationContext, UserLanding::class.java)
                                startActivity(intent)
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {

                        }
                    })
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
        // [END sign_in_with_email]
    }

    private fun sendEmailVerification() {
        // [START send_email_verification]
        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                // Email Verification sent
            }
        // [END send_email_verification]
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}