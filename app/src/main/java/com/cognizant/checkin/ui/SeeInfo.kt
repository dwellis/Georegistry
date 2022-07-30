package com.cognizant.checkin.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.cognizant.checkin.R
import com.cognizant.checkin.databinding.ActivitySeeInfoBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

lateinit var binding: ActivitySeeInfoBinding

private lateinit var database : DatabaseReference
private lateinit var accounts : DatabaseReference
private lateinit var account : DatabaseReference
private lateinit var locations : DatabaseReference

class SeeInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySeeInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.reference
        accounts = Firebase.database.reference.child("accounts")
        account = accounts.child(Firebase.auth.currentUser?.uid.toString())


        var firstName = ""
        var lastName = ""
        var birthday = ""
        var phone = ""
        var email = ""
        var address = ""

        account.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val registers = snapshot.child("registers").child(intent.extras?.get("UID").toString())
                binding.seeInfoFirstName.text = registers.child("firstName").value.toString()
                binding.seeInfoLastName.text = registers.child("lastName").value.toString()
                binding.seeInfoBirthday.text = registers.child("birthday").value.toString()
                binding.seeInfoPhone.text = registers.child("phone").value.toString()
                binding.seeInfoEmail.text = registers.child("email").value.toString()
                binding.seeInfoAddress.text = registers.child("address").value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        binding.seeInfoButtonDelete.setOnClickListener {
            account.child("registers").child(intent.extras?.get("UID").toString()).removeValue()
            accounts.child(intent.extras?.get("UID").toString()).child("registered").setValue(false)
            accounts.child(intent.extras?.get("UID").toString()).child("isComplete").setValue(false)
            Toast.makeText(applicationContext, "Register Deleted", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, RegistrarLanding::class.java)
            startActivity(intent)
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
                var homeIntent = Intent(this, RegistrarLanding::class.java)
                startActivity(homeIntent)
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