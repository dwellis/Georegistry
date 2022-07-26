package com.example.checkin.utils

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.checkin.sendGeofenceEnteredNotification
import com.example.checkin.ui.binding
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.math.log

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    private lateinit var database: DatabaseReference
    private lateinit var accounts : DatabaseReference
    private lateinit var account : DatabaseReference
    private lateinit var locations : DatabaseReference
    private lateinit var registered : DatabaseReference




    override fun onReceive(context: Context?, intent: Intent?) {

        database = Firebase.database.reference
        accounts = Firebase.database.reference.child("accounts")
        account = accounts.child(Firebase.auth.currentUser?.uid.toString())
        locations = database.child("locations")
        registered = account.child("registered")

        var subscribedID = ""


        // get data snapshot
        account.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                subscribedID = snapshot.child("subscribed").value.toString()

                accounts.child(subscribedID).child("registers").child(Firebase.auth.uid.toString()).child("name").setValue(
                    snapshot.child("firstName").value.toString() + " " + snapshot.child("lastName").value.toString()
                )
                accounts.child(subscribedID).child("registers").child(Firebase.auth.uid.toString()).child("birthday").setValue(
                    snapshot.child("birthday").value.toString()
                )
                accounts.child(subscribedID).child("registers").child(Firebase.auth.uid.toString()).child("isFormComplete").setValue(
                    false
                )


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        val geofencingEvent = GeofencingEvent.fromIntent(intent!!)
        val geofenceTransition = geofencingEvent?.geofenceTransition
        var isRegistered = false
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Log.d(TAG, "Geofence entered")


            Log.d(TAG, "onReceive: registered is $isRegistered before")

            registered.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    isRegistered = snapshot.value.toString().toBoolean()
                    Log.d(TAG, "onDataChange: registered is $isRegistered")
                }

                override fun onCancelled(error: DatabaseError) {
                    isRegistered = false
                }
            })


            if(!isRegistered) {

                Log.d(TAG, "User not registered. Sending notification")

                val notificationManager = ContextCompat.getSystemService(
                    context!!,
                    NotificationManager::class.java
                ) as NotificationManager

                notificationManager.sendGeofenceEnteredNotification(context)

                account.child("registered").setValue(true)

            }
        }
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Log.d(TAG, "Geofence exited")

        }
    }

    companion object {
        private const val TAG = "GeofenceBroadcastReceiv"
    }
}