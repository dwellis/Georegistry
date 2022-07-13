package com.example.checkin


import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.checkin.databinding.ActivityManageBinding
import com.example.checkin.ui.dialogs.AddCheckIn
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.FileInputStream
import java.util.*


class ManageActivity : AppCompatActivity() {

    lateinit var binding: ActivityManageBinding
    private lateinit var database: DatabaseReference

    lateinit var d_lat : String
    lateinit var d_long : String
    lateinit var d_title : String
    lateinit var d_desc : String

    companion object {
        private const val TAG = "ManageActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.reference



        // Initialize the AutocompleteSupportFragment.
//        val autocompleteFragment =
//            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?
//
//        autocompleteFragment!!.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME))
//
//        autocompleteFragment!!.setOnPlaceSelectedListener(object : PlaceSelectionListener {
//            override fun onPlaceSelected(place: Place) {
//                // TODO: Get info about the selected place.
//                Log.i(TAG, "Place: " + place.name + ", " + place.id)
//            }
//
//            override fun onError(status: Status) {
//                // TODO: Handle the error.
//                Log.i(TAG, "An error occurred: $status")
//            }
//        })


        binding.manageButtonAddCheckIn.setOnClickListener {


            val dialogBuilder = AlertDialog.Builder(this)

            val ll = LinearLayout(this)
            ll.orientation = android.widget.LinearLayout.VERTICAL

            val i_lat = EditText(this)
            i_lat.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
            i_lat.hint = "Latitude"

            val i_long = EditText(this)
            i_long.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
            i_long.hint = "Longitude"

            val i_title = EditText(this)
            i_title.inputType = InputType.TYPE_CLASS_TEXT
            i_title.hint = "Title"

            val i_desc = EditText(this)
            i_desc.inputType = InputType.TYPE_CLASS_TEXT
            i_desc.hint = "Description"


            ll.addView(i_lat)
            ll.addView(i_long)
            ll.addView(i_title)
            ll.addView(i_desc)

            dialogBuilder.setView(ll)


            dialogBuilder.setMessage("Please fill in the information below")
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton("Ok", DialogInterface.OnClickListener {
                        dialog, id ->
                    d_lat = i_lat.text.toString()
                    d_long = i_long.text.toString()
                    d_title = i_title.text.toString()
                    d_desc = i_title.text.toString()
                    Toast.makeText(this, "New Check In:\n$d_title\n$d_desc\n$d_lat, $d_long", Toast.LENGTH_SHORT).show()
                    finish()
                })
                // negative button text and action
                .setNegativeButton("Back", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })

            //dialogBuilder.setView(R.layout.dialog_add_check_in)



            val alert = dialogBuilder.create()
            alert.setTitle("Add a check in")
            alert.show()

            val toast = Toast.makeText(this, "Add Check In clicked", Toast.LENGTH_SHORT)
            toast.show()
        }
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

    private fun showAddCheckInDialog() {
        AddCheckIn().show(supportFragmentManager, "AddCheckIn")
    }
}