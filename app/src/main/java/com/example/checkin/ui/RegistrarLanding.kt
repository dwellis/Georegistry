package com.example.checkin.ui

import android.app.NotificationManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.checkin.R
import com.example.checkin.databinding.ActivityRegistrarLandingBinding
import com.example.checkin.sendGeofenceEnteredAdminNotification
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegistrarLanding : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarLandingBinding
    private lateinit var database: DatabaseReference
    private lateinit var accounts: DatabaseReference
    private lateinit var account: DatabaseReference
    private lateinit var registers: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Firebase.database.reference
        accounts = Firebase.database.reference.child("accounts")
        account = accounts.child(Firebase.auth.currentUser?.uid.toString())
        registers = account.child("registers")

        binding = ActivityRegistrarLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        account.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child("firstName").value != null) {
                    binding.registrarLandingWelcomeText.text = "Hello, ${snapshot.child("firstName").value.toString()}"
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

        binding.registrarLandingButtonSeeAll.setOnClickListener {
            val intent = Intent(this, ManageActivity::class.java)
            startActivity(intent)
        }

        accounts.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val childCount = snapshot.child(Firebase.auth.currentUser?.uid.toString()).child("registers").childrenCount
                val registers = snapshot.child(Firebase.auth.currentUser?.uid.toString()).child("registers")


                    //send notification
                    val notificationManager = ContextCompat.getSystemService(
                        applicationContext,
                        NotificationManager::class.java
                    ) as NotificationManager

                    notificationManager.sendGeofenceEnteredAdminNotification(applicationContext)


                Log.d(TAG, "onDataChange: $childCount")
                if(registers.hasChildren()) {
                    registers.children.forEach {

                        val subscriberID = it.key.toString()
                        val isFormComplete = it.child("isFormComplete").value.toString().toBoolean()
                        val name = it.child("name").value.toString()
                        val birthday = it.child("birthday").value.toString()

                        val tvName = TextView(applicationContext)
                        tvName.text = name

                        val tvBirthday = TextView(applicationContext)
                        tvBirthday.text = birthday

                        val isFormCompleted = CheckBox(applicationContext)
                        isFormCompleted.isChecked = isFormComplete
                        isFormCompleted.isClickable = false

                        val seeDetails = Button(applicationContext)
                        seeDetails.text = "See Information"

                        seeDetails.setOnClickListener {
                            val intent = Intent(applicationContext, SeeInfo::class.java)
                            intent.putExtra("UID", subscriberID)
                            startActivity(intent)
                        }

                        tvName.setPadding(0, 100, 0, 0)
                        tvName.textSize = 20f
                        isFormCompleted.text = "Form Completed"

                        binding.registrarLandingLl.addView(tvName)
                        binding.registrarLandingLl.addView(tvBirthday)
                        binding.registrarLandingLl.addView(isFormCompleted)

                        if(isFormComplete) {
                            binding.registrarLandingLl.addView(seeDetails)
                        }
                    }
                }

            }
            override fun onCancelled(error: DatabaseError) {
                val noneTv = TextView(applicationContext)
                noneTv.text = "No current registers"
                binding.registrarLandingLl.addView(noneTv)
            }
        })
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

    companion object {
        private const val TAG = "RegistrarLanding"
    }
}