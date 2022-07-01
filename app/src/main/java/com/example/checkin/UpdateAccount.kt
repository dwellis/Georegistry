package com.example.checkin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.example.checkin.databinding.ActivityUpdateAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UpdateAccount : AppCompatActivity() {

    private lateinit var binding : ActivityUpdateAccountBinding
    private lateinit var database: DatabaseReference

    companion object {
        private const val TAG = "UpdateAccount"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Firebase.database.reference

        binding = ActivityUpdateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // update inputs to current values
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.child("accounts").child(Firebase.auth.currentUser?.uid.toString()).child("firstName").value
                val welcomeText = "Welcome, " + snapshot.child("accounts").child(Firebase.auth.currentUser?.uid.toString()).child("firstName").value.toString() + "!"
                val fullName = snapshot.child("accounts").child(Firebase.auth.currentUser?.uid.toString()).child("firstName").value.toString() + " " + snapshot.child("accounts").child(Firebase.auth.currentUser?.uid.toString()).child("lastName").value.toString()
                val email = snapshot.child("accounts").child(Firebase.auth.currentUser?.uid.toString()).child("email").value.toString()
                val birthday = snapshot.child("accounts").child(Firebase.auth.currentUser?.uid.toString()).child("birthday").value.toString()
                val admin = snapshot.child("accounts").child(Firebase.auth.currentUser?.uid.toString()).child("admin").value.toString()

                binding.createAccountFirstNameInput.setText(snapshot.child("accounts").child(Firebase.auth.currentUser?.uid.toString()).child("firstName").value.toString())
                binding.createAccountLastNameInput.setText(snapshot.child("accounts").child(Firebase.auth.currentUser?.uid.toString()).child("lastName").value.toString())
                binding.createAccountBirthdayInput.setText(snapshot.child("accounts").child(Firebase.auth.currentUser?.uid.toString()).child("birthday").value.toString())
                binding.createAccountEmailInput.setText(snapshot.child("accounts").child(Firebase.auth.currentUser?.uid.toString()).child("email").value.toString())
                binding.createAccountAdminCheckbox.isChecked = snapshot.child("accounts").child(Firebase.auth.currentUser?.uid.toString()).child("admin").value.toString().toBoolean()


                Log.d(UpdateAccount.TAG, "onDataChange: ${value.toString()}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(UpdateAccount.TAG, "onCancelled: Failed to read value")
            }
        })
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
                R.id.main_menu_profile -> {
                    var profileIntent = Intent(this, ProfileActivity::class.java)
                    startActivity(profileIntent)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
    }
}