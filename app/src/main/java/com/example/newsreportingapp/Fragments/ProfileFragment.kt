package com.example.newsreportingapp.Fragments

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.newsreportingapp.R

class ProfileFragment : Fragment(R.layout.profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        val profileImage = view.findViewById<ImageView>(R.id.profileImage)
        val nameText = view.findViewById<TextView>(R.id.nameText)
        val idText = view.findViewById<TextView>(R.id.idText)
        val bioText = view.findViewById<TextView>(R.id.bioText)
        val emailText = view.findViewById<EditText>(R.id.emailText)
        val contactText = view.findViewById<EditText>(R.id.contactText)
        val editProfileButton = view.findViewById<Button>(R.id.editProfileButton)
        val saveButton = view.findViewById<Button>(R.id.saveButton)

        // Load existing data
        nameText.text = "Mathew Jorden"
        idText.text = "News Reporter | ID: 1EX456"
        bioText.text = "Award-winning journalist covering global news. Passionate about storytelling and uncovering the truth."

        // Edit Profile Button Action
        editProfileButton.setOnClickListener {
            emailText.isEnabled = true
            contactText.isEnabled = true
            Toast.makeText(requireContext(), "You can now edit your contact details", Toast.LENGTH_SHORT).show()
        }

        // Save Button Action
        saveButton.setOnClickListener {
            val email = emailText.text.toString()
            val contact = contactText.text.toString()

            // Save data (should be stored in a database or SharedPreferences)
            Toast.makeText(requireContext(), "Profile Updated", Toast.LENGTH_SHORT).show()

            // Disable editing after saving
            emailText.isEnabled = false
            contactText.isEnabled = false
        }
    }
}
