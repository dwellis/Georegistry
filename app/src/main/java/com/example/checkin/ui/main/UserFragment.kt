package com.example.checkin.ui.main

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.checkin.*
import com.google.firebase.auth.FirebaseAuth

class UserFragment : Fragment() {

    companion object {
        fun newInstance() = UserFragment()
    }

    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val inflater: MenuInflater = inflater
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (FirebaseAuth.getInstance().currentUser == null) {
            return when (item.itemId) {
                R.id.main_menu_home -> {
                    var homeIntent = Intent(context, MainActivity::class.java)
                    startActivity(homeIntent)
                    true
                }
                R.id.main_menu_maps -> {
                    var mapsIntent = Intent(context, MapsActivity::class.java)
                    startActivity(mapsIntent)
                    true
                }
                R.id.main_menu_profile -> {
                    var loginIntent = Intent(context, LoginActivity::class.java)
                    startActivity(loginIntent)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        } else {
            return when (item.itemId) {
                R.id.main_menu_home -> {
                    var homeIntent = Intent(context, MainActivity::class.java)
                    startActivity(homeIntent)
                    true
                }
                R.id.main_menu_maps -> {
                    var mapsIntent = Intent(context, MapsActivity::class.java)
                    startActivity(mapsIntent)
                    true
                }
                R.id.main_menu_profile -> {
                    var profileIntent = Intent(context, ProfileActivity::class.java)
                    startActivity(profileIntent)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }

    }
}