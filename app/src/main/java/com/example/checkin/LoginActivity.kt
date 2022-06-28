package com.example.checkin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.checkin.databinding.ActivityLoginBinding
import com.example.checkin.databinding.ActivityMapsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)
        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.login.setOnClickListener {
            Log.d(TAG, "Login clicked")
            signIn(binding.username.text.toString(), binding.password.text.toString())
        }

        binding.register.setOnClickListener {
            Log.d(TAG, "Register clicked")
            createAccount(binding.username.text.toString(), binding.password.text.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload();
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.main_menu_home -> {
                var homeIntent = Intent(this, MainActivity::class.java)
                startActivity(homeIntent)
                true
            }
            R.id.main_menu_maps -> {
                var mapsIntent = Intent(this, MapsActivity::class.java)
                startActivity(mapsIntent)
                true
            }
            R.id.main_menu_forms -> {
                var formsIntent = Intent(this, FormsActivity::class.java)
                startActivity(formsIntent)
                true
            }
            R.id.main_menu_profile -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                    var homeIntent = Intent(this, MainActivity::class.java)
                    startActivity(homeIntent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
        // [END create_user_with_email]
    }

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    Toast.makeText(baseContext, "Signed In.", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    updateUI(user)
                    var homeIntent = Intent(this, MainActivity::class.java)
                    startActivity(homeIntent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
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

    private fun updateUI(user: FirebaseUser?) {
        binding.username.isVisible = false
        binding.password.isVisible = false
        binding.login.isVisible = false
        binding.register.isVisible = false
    }

    private fun reload() {

    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}