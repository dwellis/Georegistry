package com.cognizant.checkin.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.core.text.set
import com.cognizant.checkin.R
import com.cognizant.checkin.databinding.ActivityUserFormBinding
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

    lateinit var subscribedID: String
    lateinit var subscriberData: DatabaseReference

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.reference
        accounts = Firebase.database.reference.child("accounts")
        account = accounts.child(Firebase.auth.currentUser?.uid.toString())
        locations = database.child("locations")

        fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

        // update db with new info
        accounts.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                // the uid of the subscribed registrar
                subscribedID = snapshot.child(Firebase.auth.uid.toString()).child("subscribed").value.toString()
                // the subscribed users location in the database that is accessible by the registrar
                val subscriber = snapshot.child(subscribedID).child("registers").child(Firebase.auth.uid.toString())
                subscriberData = accounts.child(subscribedID).child("registers").child(Firebase.auth.uid.toString())

                Log.d(TAG, "onDataChange - isFormComplete: ${subscriber.child("isFormComplete").value.toString()}")

            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

        binding.userFormButtonSubmit.setOnClickListener {


            // set the user data in the db for registrar
            subscriberData.child("isFormComplete").setValue(true)
            subscriberData.child("firstName").setValue(binding.userFormFirstNameTv.text.toString())
            subscriberData.child("lastName").setValue(binding.userFormLastNameActv.text.toString())
            subscriberData.child("birthday").setValue(binding.userFormEditBirthday.text.toString())
            subscriberData.child("phone").setValue(binding.userFormEditPhone.text.toString())
            subscriberData.child("email").setValue(binding.userFormEditEmail.text.toString())
            subscriberData.child("address").setValue(binding.userFormEditAddress.text.toString())

            // set flag to show user has completed form for user landing
            account.child("isFormComplete").setValue(true)

            val intent = Intent(applicationContext, UserLanding::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.main_menu_home -> {
                var homeIntent = Intent(this, UserLanding::class.java)
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

    companion object {
        private const val TAG = "UserForm"
    }
}