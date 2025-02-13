package com.example.newsreportingapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Profile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        // Initialize views
        val profileImage = findViewById<ImageView>(R.id.profileImage)
        val nameText = findViewById<TextView>(R.id.nameText)
        val idText = findViewById<TextView>(R.id.idText)
        val bioText = findViewById<TextView>(R.id.bioText)
        val emailText = findViewById<EditText>(R.id.emailText)
        val contactText = findViewById<EditText>(R.id.contactText)
        val editProfileButton = findViewById<Button>(R.id.editProfileButton)
        val saveButton = findViewById<Button>(R.id.saveButton)

        // Load existing data (if any)
        nameText.text = "Mathew Jorden"
        idText.text = "News Reporter | ID: 1EX456"
        bioText.text = "Award-winning journalist covering global news. Passionate about storytelling and uncovering the truth."

        // Edit Profile Button Action
        editProfileButton.setOnClickListener {
            emailText.isEnabled = true
            contactText.isEnabled = true
            Toast.makeText(this, "You can now edit your contact details", Toast.LENGTH_SHORT).show()
        }

        // Save Button Action
        saveButton.setOnClickListener {
            val email = emailText.text.toString()
            val contact = contactText.text.toString()

            // Save data (this should ideally be stored in a database or SharedPreferences)
            Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()

            // Disable editing after saving
            emailText.isEnabled = false
            contactText.isEnabled = false
        }
    }
}
