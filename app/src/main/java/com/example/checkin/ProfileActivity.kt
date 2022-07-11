package com.example.checkin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.example.checkin.databinding.ActivityMapsBinding
import com.example.checkin.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlin.math.log

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var database: DatabaseReference

    companion object {
        private const val TAG = "ProfileActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Firebase.database.reference

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.child("accounts").child(Firebase.auth.currentUser?.uid.toString()).child("firstName").value
                val welcomeText = "Welcome, " + snapshot.child("accounts").child(Firebase.auth.currentUser?.uid.toString()).child("firstName").value.toString() + "!"
                val fullName = snapshot.child("accounts").child(Firebase.auth.currentUser?.uid.toString()).child("firstName").value.toString() + " " + snapshot.child("accounts").child(Firebase.auth.currentUser?.uid.toString()).child("lastName").value.toString()
                val email = snapshot.child("accounts").child(Firebase.auth.currentUser?.uid.toString()).child("email").value.toString()
                val birthday = snapshot.child("accounts").child(Firebase.auth.currentUser?.uid.toString()).child("birthday").value.toString()
                val admin = snapshot.child("accounts").child(Firebase.auth.currentUser?.uid.toString()).child("admin").value.toString()

                binding.profileWelcome.text = welcomeText
                binding.profileNameText.text = fullName
                binding.profileEmailText.text = email
                binding.profileBirthdayText.text = birthday

                // if admin enable admin button
                if(admin.toBoolean()) {
                    binding.profileButtonOption.visibility = android.view.View.VISIBLE
                    binding.profileButtonOption.isClickable = true
                    binding.profileButtonOption.text = "Admin"
                    binding.profileButtonOption.setOnClickListener {
                        goToAdmin()
                    }
                } else {
                    binding.profileButtonOption.visibility = android.view.View.VISIBLE
                    binding.profileButtonOption.isClickable = true
                    binding.profileButtonOption.text = "User"
                    binding.profileButtonOption.setOnClickListener {
                        goToUser()
                    }
                }

                Log.d(TAG, "onDataChange: ${value.toString()}")
            }


            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "onCancelled: Failed to read value")
            }
        })

        // check if admin and enable button






        binding.profileButtonDelete.setOnClickListener {
            deleteUser()
        }

        binding.profileButtonSignOut.setOnClickListener {
            signOut()
        }

        binding.profileButtonUpdate.setOnClickListener {
            val intent = Intent(this, UpdateAccount::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(FirebaseAuth.getInstance().currentUser == null) {
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
//                R.id.main_menu_forms -> {
//                    var formsIntent = Intent(this, FormsActivity::class.java)
//                    startActivity(formsIntent)
//                    true
//                }
                R.id.main_menu_profile -> {
                    var loginIntent = Intent(this, LoginActivity::class.java)
                    startActivity(loginIntent)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
        else {
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
//                R.id.main_menu_forms -> {
//                    var formsIntent = Intent(this, FormsActivity::class.java)
//                    startActivity(formsIntent)
//                    true
//                }
                R.id.main_menu_profile -> {
                    var profileIntent = Intent(this, ProfileActivity::class.java)
                    startActivity(profileIntent)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
    }

    private fun resetPassword() {
        if(Firebase.auth.currentUser != null) {
            val emailAddress = Firebase.auth.currentUser!!.email.toString()
            Firebase.auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    Log.d(ProfileActivity.TAG, "Password reset email sent")
                    Toast.makeText(this,"Password reset email sent", Toast.LENGTH_SHORT )
                }
            }
        }
    }

    private fun deleteUser() {
        val user = Firebase.auth.currentUser!!

        user.delete().addOnCompleteListener { task ->
            if(task.isSuccessful) {
                Log.d(TAG, "User account deleted.")

                database.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.child("accounts").child(Firebase.auth.currentUser?.uid.toString()).ref.removeValue()


                        Log.d(TAG, "onDataChange: ${snapshot.child("accounts").child(Firebase.auth.currentUser?.uid.toString()).toString()} Removed")
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d(TAG, "onCancelled: Failed to read value")
                    }
                })

                Toast.makeText(this,"User account deleted", Toast.LENGTH_SHORT )
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun signOut() {
        Firebase.auth.signOut()
        Toast.makeText(this, "User signed out", Toast.LENGTH_SHORT)
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
    }

    private fun goToAdmin() {
        var intent = Intent(this, Admin::class.java)
        startActivity(intent)
    }

    private fun goToUser() {
        var intent = Intent(this, User::class.java)
        startActivity(intent)
    }
}