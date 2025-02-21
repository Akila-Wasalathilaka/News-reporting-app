package com.example.newsreportingapp.Fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.newsreportingapp.Post
import com.example.newsreportingapp.R
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class AddNewsFragment : Fragment(R.layout.add_news) {

    private lateinit var topicInput: EditText
    private lateinit var captionInput: EditText
    private lateinit var sendButton: Button
    private lateinit var deleteButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        topicInput = view.findViewById(R.id.topicInput)
        captionInput = view.findViewById(R.id.captionInput)
        sendButton = view.findViewById(R.id.sendButton)
        deleteButton = view.findViewById(R.id.deleteButton)

        sendButton.setOnClickListener {
            uploadPost()
        }

        deleteButton.setOnClickListener {
            resetFields()
        }
    }

    private fun uploadPost() {
        val topic = topicInput.text.toString().trim()
        val caption = captionInput.text.toString().trim()

        // Check if the required fields are not empty
        if (topic.isEmpty() || caption.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Show a Toast to confirm that the upload process is starting
        Toast.makeText(requireContext(), "Uploading Post...", Toast.LENGTH_SHORT).show()

        try {
            // Create a new post object
            val postId = UUID.randomUUID().toString()
            val database = FirebaseDatabase.getInstance().reference
            val post = Post(postId, topic, caption, "")

            // Store data in Firebase Realtime Database
            database.child("posts").child(postId).setValue(post).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(requireContext(), "Post uploaded successfully", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to upload post: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error during upload: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetFields() {
        topicInput.text.clear()
        captionInput.text.clear()
    }
}
