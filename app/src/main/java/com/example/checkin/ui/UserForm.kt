package com.example.checkin.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.checkin.databinding.ActivityUserFormBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserForm : AppCompatActivity() {

    private lateinit var binding : ActivityUserFormBinding

    private lateinit var database : DatabaseReference
    private lateinit var accounts : DatabaseReference
    private lateinit var account : DatabaseReference
    private lateinit var locations : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.reference
        accounts = Firebase.database.reference.child("accounts")
        account = accounts.child(Firebase.auth.currentUser?.uid.toString())
        locations = database.child("locations")



        binding.userFormButtonSubmit.setOnClickListener {


            // update db with new info
            accounts.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val subscribedID = snapshot.child(Firebase.auth.uid.toString()).child("subscribed").value.toString()
                    Log.d(TAG, "onDataChange: subscribed $subscribedID")
                    accounts.child(subscribedID).child("registers").child(Firebase.auth.uid.toString()).child("isFormComplete").setValue(true)
                    accounts.child(subscribedID).child("registers").child(Firebase.auth.uid.toString()).child("firstName").setValue(
                        binding.userFormFirstNameActv.text.toString()
                    )
                    accounts.child(subscribedID).child("registers").child(Firebase.auth.uid.toString()).child("lastName").setValue(
                        binding.userFormLastNameActv.text.toString()
                    )
                    accounts.child(subscribedID).child("registers").child(Firebase.auth.uid.toString()).child("birthday").setValue(
                        binding.userFormEditBirthday.text.toString()
                    )
                    accounts.child(subscribedID).child("registers").child(Firebase.auth.uid.toString()).child("phone").setValue(
                        binding.userFormEditPhone.text.toString()
                    )
                    accounts.child(subscribedID).child("registers").child(Firebase.auth.uid.toString()).child("email").setValue(
                        binding.userFormEditEmail.text.toString()
                    )
                    accounts.child(subscribedID).child("registers").child(Firebase.auth.uid.toString()).child("address").setValue(
                        binding.userFormEditAddress.text.toString()
                    )

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

            val intent = Intent(applicationContext, UserLanding::class.java)
            startActivity(intent)

        }

    }

    companion object {
        private const val TAG = "UserForm"
    }
}