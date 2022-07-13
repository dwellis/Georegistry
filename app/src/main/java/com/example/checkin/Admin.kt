package com.example.checkin

import android.app.Dialog
import android.app.ProgressDialog.show
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.example.checkin.databinding.ActivityUpdateAccountBinding
import com.example.checkin.databinding.FragmentAdminBinding
import com.example.checkin.ui.dialogs.AddCheckIn
import com.example.checkin.ui.main.AdminFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Admin : AppCompatActivity() {

    private lateinit var binding : FragmentAdminBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Firebase.database.reference
        binding = FragmentAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContentView(R.layout.activity_admin)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AdminFragment.newInstance())
                .commitNow()
        }

        // set button actions
        binding.addCheckIn.setOnClickListener {
            showAddCheckInDialog()
            val toast = Toast.makeText(this, "Add Check In clicked", Toast.LENGTH_SHORT)
            toast.show()
        }
    }




    private fun showAddCheckInDialog() {
        val aci = AddCheckIn()
        aci.show(supportFragmentManager, "AddCheckIn")
    }

}