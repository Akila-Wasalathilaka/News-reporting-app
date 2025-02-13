package com.example.newsreportingapp.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.newsreportingapp.MainActivity
import com.example.newsreportingapp.R

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var forgotPasswordTextView: TextView
    private lateinit var signInButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity) // Link to the login_activity.xml layout

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView)
        signInButton = findViewById(R.id.signInButton)

        // Set up the sign-in button click listener
        signInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Basic validation (you can add more validation as needed)
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Proceed with the login logic (e.g., authenticate the user)
                // For now, we'll just simulate a successful login
                val intent = Intent(this, MainActivity::class.java) // Go to MainActivity after login
                startActivity(intent)
                finish() // Close the login activity
            } else {
                // Show an error message if the fields are empty
                if (email.isEmpty()) {
                    emailEditText.error = "Email is required"
                }
                if (password.isEmpty()) {
                    passwordEditText.error = "Password is required"
                }
            }
        }

        // Set up the forgot password click listener (optional)
        forgotPasswordTextView.setOnClickListener {
            // Navigate to forgot password activity or show dialog
            // For now, we'll just show a message
            // You can implement a new activity or dialog here
        }
    }
}
