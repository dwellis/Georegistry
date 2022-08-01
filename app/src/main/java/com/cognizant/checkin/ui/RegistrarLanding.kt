package com.cognizant.checkin.ui

import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.cognizant.checkin.R
import com.cognizant.checkin.databinding.ActivityRegistrarLandingBinding
import com.cognizant.checkin.sendGeofenceEnteredAdminNotification
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
                val registers = snapshot.child(Firebase.auth.currentUser?.uid.toString()).child("registers")
                if(registers.hasChildren()) {
                    registers.children.forEach {
                        val subscriberID = it.key.toString()
                        val isFormComplete = it.child("isFormComplete").value.toString().toBoolean()
                        val name = it.child("name").value.toString()
                        val birthday = it.child("birthday").value.toString()
                        val isNotified = it.child("isNotificationSent").value.toString().toBoolean()

                        val registerCardLL = LinearLayout(applicationContext)
                        registerCardLL.orientation = LinearLayout.VERTICAL
                        val params = RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT)
                        params.setMargins(10,30,10,30)

                        val registerCard = CardView(applicationContext)
                        registerCard.radius = 15f
                        registerCard.setCardBackgroundColor(Color.parseColor("#f3f3f3"))
                        registerCard.setContentPadding(36,36,36,36)
                        registerCard.layoutParams = params
                        registerCard.cardElevation = 15f

                        val tvName = TextView(applicationContext)
                        tvName.text = name
                        tvName.textSize = 20f

                        val tvBirthday = TextView(applicationContext)
                        tvBirthday.text = birthday
                        tvBirthday.setPadding(0, 20, 0, 20)

                        val isFormCompleted = CheckBox(applicationContext)
                        isFormCompleted.isChecked = isFormComplete
                        isFormCompleted.isClickable = false
                        isFormCompleted.textSize = 16f

                        val seeDetails = TextView(applicationContext)
                        seeDetails.text = "SEE INFO"
                        seeDetails.setTextColor(Color.DKGRAY)
                        seeDetails.setPadding(0, 40, 0, 0)
                        seeDetails.gravity = Gravity.RIGHT

                        seeDetails.setOnClickListener {
                            val intent = Intent(applicationContext, SeeInfo::class.java)
                            intent.putExtra("UID", subscriberID)
                            startActivity(intent)
                        }

                        val removeRegister = TextView(applicationContext)
                        removeRegister.text = "REMOVE REGISTER"
                        removeRegister.setTextColor(Color.DKGRAY)
                        removeRegister.setPadding(0, 40, 0, 0)
                        removeRegister.gravity = Gravity.RIGHT

                        removeRegister.setOnClickListener {
                            account.child("registers").child(subscriberID).removeValue()
                            accounts.child(subscriberID).child("registered").setValue(false)
                            accounts.child(subscriberID).child("isFormComplete").setValue(false)
                            Toast.makeText(applicationContext, "Register Deleted", Toast.LENGTH_SHORT).show()
                        }

                        isFormCompleted.text = "Form Completed"

                        registerCardLL.addView(tvName)
                        registerCardLL.addView(tvBirthday)
                        registerCardLL.addView(isFormCompleted)
                        registerCardLL.addView(removeRegister)

                        if(isFormComplete ) {
                            // add button to see register info
                            registerCardLL.addView(seeDetails)
                            // send notification if they are not notified
                            if (!isNotified) {
                                //send notification
                                val notificationManager = ContextCompat.getSystemService(
                                    applicationContext,
                                    NotificationManager::class.java
                                ) as NotificationManager

                                notificationManager.sendGeofenceEnteredAdminNotification(
                                    applicationContext
                                )

                                // change admin notification status in db
                                account.child("registers").child(subscriberID)
                                    .child("isNotificationSent").setValue(true)
                            }
                        }

                        registerCard.addView(registerCardLL)

                        binding.registrarLandingLl.addView(registerCard)
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