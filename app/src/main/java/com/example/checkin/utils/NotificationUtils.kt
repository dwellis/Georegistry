package com.example.checkin

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.checkin.ui.RegistrarLanding
import com.example.checkin.ui.UserForm
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private const val NOTIFICATION_ID = 33
private const val CHANNEL_ID = "GeofenceChannel"
private lateinit var registered : DatabaseReference
var isRegistered = false

/*
 * We need to create a NotificationChannel associated with our CHANNEL_ID before sending a
 * notification.
 */
fun createChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.channel_name),

            NotificationManager.IMPORTANCE_HIGH
        )
            .apply {
                setShowBadge(false)
            }

        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = context.getString(R.string.notification_channel_description)

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }
}

/*
 * A Kotlin extension function for AndroidX's NotificationCompat that sends our Geofence
 * entered notification.  It sends a custom notification based on the name string associated
 * with the LANDMARK_DATA from GeofencingConstatns in the GeofenceUtils file.
 */
fun NotificationManager.sendGeofenceEnteredNotification(context: Context) {
    registered = Firebase.database.reference.child("accounts").child(Firebase.auth.uid.toString()).child("registered")
    registered.addValueEventListener(object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            isRegistered = snapshot.value.toString().toBoolean()
        }

        override fun onCancelled(error: DatabaseError) {
            isRegistered = false
        }
    })

    if(!isRegistered) {
        val contentIntent = Intent(context, UserForm::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_MUTABLE
        )
        val mapImage = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.globe_logo_black
        )
        val bigPicStyle = NotificationCompat.BigPictureStyle()
            .bigPicture(mapImage)
            .bigLargeIcon(null)

        // We use the name resource ID from the LANDMARK_DATA along with content_text to create
        // a custom message when a Geofence triggers.
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText("You have registered at a location. Please fill out this form.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(contentPendingIntent)
            .setSmallIcon(R.drawable.globe_logo_black)


        notify(NOTIFICATION_ID, builder.build())
    }
}

fun NotificationManager.sendGeofenceEnteredAdminNotification(context: Context) {
    val contentIntent = Intent(context, RegistrarLanding::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        context,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_MUTABLE
    )
    val mapImage = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.globe_logo_black
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(mapImage)
        .bigLargeIcon(null)

    // We use the name resource ID from the LANDMARK_DATA along with content_text to create
    // a custom message when a Geofence triggers.
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setContentTitle(context.getString(R.string.app_name))
        .setContentText("You have a new register")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(contentPendingIntent)
        .setSmallIcon(R.drawable.globe_logo_black)


    notify(NOTIFICATION_ID, builder.build())
}