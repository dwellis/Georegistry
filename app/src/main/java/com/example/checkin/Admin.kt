package com.example.checkin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.checkin.ui.main.AdminFragment

class Admin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AdminFragment.newInstance())
                .commitNow()
        }
    }


}