package com.example.checkin

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class FormsFragment : Fragment() {

    lateinit var button : Button
    lateinit var firstName: String
    lateinit var lastName: String
    lateinit var birthday: String
    private lateinit var database: DatabaseReference

    companion object {
        fun newInstance() = FormsFragment()
    }

    private lateinit var viewModel: FormsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        database = Firebase.database.reference

        val view: View = inflater!!.inflate(R.layout.fragment_forms, container, false)

        button = view.findViewById(R.id.forms_submit_button)


        button.setOnClickListener {
            Log.d("btnSetup", "NEW CHECK IN")
            //writeNewCheckIn("001", "01/01/2000", "test", "name")
            birthday = view.findViewById<EditText>(R.id.forms_birthday).text.toString()
            firstName = view.findViewById<EditText>(R.id.forms_first_name_input).text.toString()
            lastName = view.findViewById<EditText>(R.id.forms_last_name_input).text.toString()
            writeNewCheckIn("000000", birthday, firstName, lastName)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FormsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    @IgnoreExtraProperties
    data class CheckIn(val birthday: String? = null, val firstName: String? = null, val lastName: String? = null) {
        // Null default values create a no-argument default constructor, which is needed
        // for deserialization from a DataSnapshot.
    }

    fun writeNewCheckIn(checkInId : String, birthday: String?, firstName: String?, lastName: String?) {
        val checkIn = CheckIn(birthday, firstName, lastName)
        database.child("checkIns").child(checkInId).setValue(checkIn)
    }

}