package com.example.checkin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.checkin.ui.main.UserFragment

class User : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, UserFragment.newInstance())
                .commitNow()
        }
    }
}