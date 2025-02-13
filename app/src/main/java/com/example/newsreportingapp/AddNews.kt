package com.example.newsreportingapp

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class AddNews : AppCompatActivity() {

    private lateinit var selectedImageView: ImageView
    private lateinit var addImageButton: ImageButton
    private lateinit var deleteButton: ImageButton
    private lateinit var sendButton: Button
    private lateinit var captionInput: EditText
    private lateinit var capturePhotoButton: Button

    private var selectedImageUri: Uri? = null
    private var cameraImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_news)

        // Initialize views
        selectedImageView = findViewById(R.id.selectedImageView)
        addImageButton = findViewById(R.id.addImageButton)
        deleteButton = findViewById(R.id.deleteButton)
        sendButton = findViewById(R.id.sendButton)
        captionInput = findViewById(R.id.captionInput)
        capturePhotoButton = findViewById(R.id.capturePhotoButton)

        // Add image from gallery
        addImageButton.setOnClickListener {
            openGallery()
        }

        // Capture photo from camera
        capturePhotoButton.setOnClickListener {
            openCamera()
        }

        // Delete selected image
        deleteButton.setOnClickListener {
            selectedImageUri = null
            selectedImageView.setImageURI(null)
            selectedImageView.visibility = ImageView.GONE
            addImageButton.visibility = ImageButton.VISIBLE
            deleteButton.visibility = ImageButton.GONE
        }

        // Send button
        sendButton.setOnClickListener {
            val caption = captionInput.text.toString().trim()
            if (selectedImageUri == null) {
                Toast.makeText(this, "Please select or capture an image", Toast.LENGTH_SHORT).show()
            } else if (caption.isEmpty()) {
                Toast.makeText(this, "Please write a caption", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "News posted successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    // Open Gallery
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                selectedImageUri = uri
                selectedImageView.setImageURI(uri)
                selectedImageView.visibility = ImageView.VISIBLE
                addImageButton.visibility = ImageButton.GONE
                deleteButton.visibility = ImageButton.VISIBLE
            }
        }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    // Open Camera
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            if (success) {
                selectedImageUri = cameraImageUri
                selectedImageView.setImageURI(cameraImageUri)
                selectedImageView.visibility = ImageView.VISIBLE
                addImageButton.visibility = ImageButton.GONE
                deleteButton.visibility = ImageButton.VISIBLE
            }
        }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            }
            cameraImageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

            cameraImageUri?.let { cameraLauncher.launch(it) }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 102)
        }
    }

    // Handle permission request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 102 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            Toast.makeText(this, "Camera permission is required to capture photos", Toast.LENGTH_SHORT).show()
        }
    }
}