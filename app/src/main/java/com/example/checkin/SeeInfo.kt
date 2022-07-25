package com.example.checkin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.adapters.ToolbarBindingAdapter
import com.example.checkin.databinding.ActivitySeeInfoBinding
import com.example.checkin.databinding.ActivityUserLandingBinding
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
        locations = database.child("locations")

        var firstName = ""
        var lastName = ""
        var birthday = ""
        var phone = ""
        var email = ""
        var address = ""




        account.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.seeInfoFirstName.text = snapshot.child("registers").child(intent.extras?.get("UID").toString()).child("firstName").value.toString()
                binding.seeInfoLastName.text = snapshot.child("registers").child(intent.extras?.get("UID").toString()).child("lastName").value.toString()
                binding.seeInfoBirthday.text = snapshot.child("registers").child(intent.extras?.get("UID").toString()).child("birthday").value.toString()
                binding.seeInfoPhone.text = snapshot.child("registers").child(intent.extras?.get("UID").toString()).child("phone").value.toString()
                binding.seeInfoEmail.text = snapshot.child("registers").child(intent.extras?.get("UID").toString()).child("email").value.toString()
                binding.seeInfoAddress.text = snapshot.child("registers").child(intent.extras?.get("UID").toString()).child("address").value.toString()



            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        binding.seeInfoButtonDelete.setOnClickListener {
            account.child("registers").child(intent.extras?.get("UID").toString()).removeValue()
            Toast.makeText(applicationContext, "Register Deleted", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, RegistrarLanding::class.java)
            startActivity(intent)
        }

    }
}