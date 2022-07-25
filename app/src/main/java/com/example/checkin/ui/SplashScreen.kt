package com.example.checkin.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import com.example.checkin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SplashScreen : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var accounts : DatabaseReference
    private lateinit var account : DatabaseReference
    private var adminFlag : Boolean = false

    companion object {
        private const val TAG = "SplashScreen"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        database = Firebase.database.reference
        accounts = Firebase.database.reference.child("accounts")
        account = accounts.child(Firebase.auth.currentUser?.uid.toString())

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed({

            // check to see if logged in and if so, are they an admin
            if(FirebaseAuth.getInstance().currentUser == null) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {

                accounts.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        adminFlag = snapshot.child(Firebase.auth.currentUser?.uid.toString()).child("admin").value.toString().toBoolean()
                        Log.d(TAG, "onDataChange: ${snapshot.child(Firebase.auth.currentUser?.uid.toString()).child("admin").value}")

                        if(adminFlag) {
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


            }



            finish()
        }, 3000)
    }
}