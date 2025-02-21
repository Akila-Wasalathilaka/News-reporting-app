package com.example.newsreportingapp.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.newsreportingapp.R
import com.example.newsreportingapp.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment(R.layout.profile) {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Initialize UI components
        val profileImage = view.findViewById<ImageView>(R.id.profileImage)
        val nameText = view.findViewById<TextView>(R.id.nameText)
        val emailText = view.findViewById<TextView>(R.id.emailText)
        val bioText = view.findViewById<TextView>(R.id.bioText)
        val editProfileButton = view.findViewById<Button>(R.id.editProfileButton)
        val logoutButton = view.findViewById<Button>(R.id.logoutButton)

        // Check if the user is logged in via Google
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Display the user's details
            loadUserData(currentUser, profileImage, nameText, emailText, bioText)
        }

        // Edit Profile Button Action
        editProfileButton.setOnClickListener {
            Toast.makeText(requireContext(), "You can edit your profile now", Toast.LENGTH_SHORT).show()
        }
        //Log Out Button
        logoutButton.setOnClickListener{
            auth.signOut()
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun loadUserData(
        user: FirebaseUser,
        profileImage: ImageView,
        nameText: TextView,
        emailText: TextView,
        bioText: TextView
    ) {
        // Display user's profile picture and name
        Picasso.get().load(user.photoUrl).into(profileImage)
        nameText.text = user.displayName ?: "No name available"
        emailText.text = user.email ?: "No email available"
        bioText.text = "User bio is not set"  // Set bio if available in Firestore or let it be default
    }

    private fun saveUserData(email: String, bio: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            // Explicitly cast the map to Map<String, Any>
            val updatedData: Map<String, Any> = hashMapOf(
                "email" to email,
                "bio" to bio
            )

            db.collection("users").document(userId).update(updatedData)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Error updating profile: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
