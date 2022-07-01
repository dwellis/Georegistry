package com.example.checkin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.IgnoreExtraProperties

class FormsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forms)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, FormsFragment.newInstance())
                .commitNow()
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
                var homeIntent = Intent(this, MainActivity::class.java)
                startActivity(homeIntent)
                true
            }
            R.id.main_menu_maps -> {
                var mapsIntent = Intent(this, MapsActivity::class.java)
                startActivity(mapsIntent)
                true
            }
//            R.id.main_menu_forms -> {
//                var formsIntent = Intent(this, FormsActivity::class.java)
//                startActivity(formsIntent)
//                true
//            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}