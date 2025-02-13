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
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class AddNews : AppCompatActivity() {
    // Views
    private lateinit var selectedImageView: ImageView
    private lateinit var addImageButton: ImageButton
    private lateinit var deleteButton: ImageButton
    private lateinit var sendButton: Button
    private lateinit var captionInput: EditText
    private lateinit var capturePhotoButton: Button

    // Permissions
    private val CAMERA_PERMISSION_CODE = 100
    private val GALLERY_PERMISSION_CODE = 101
    private var imageUri: Uri? = null

    // Activity Results
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageUri = uri
            loadImageToView(uri)
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if(success) {
            imageUri?.let { loadImageToView(it) }
        }
    }

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

        // Button click handlers
        addImageButton.setOnClickListener { openGallery() }
        capturePhotoButton.setOnClickListener { openCamera() }
        deleteButton.setOnClickListener { clearImageSelection() }
        sendButton.setOnClickListener { validateAndPost() }
    }

    private fun openGallery() {
        if(checkStoragePermission()) {
            galleryLauncher.launch("image/*")
        }
    }

    private fun openCamera() {
        if(checkCameraPermission()) {
            createImageUri()?.let { uri ->
                imageUri = uri
                cameraLauncher.launch(uri)
            }
        }
    }

    private fun loadImageToView(uri: Uri) {
        selectedImageView.setImageURI(uri)
        selectedImageView.visibility = View.VISIBLE
        addImageButton.visibility = View.GONE
        deleteButton.visibility = View.VISIBLE
    }

    private fun clearImageSelection() {
        imageUri = null
        selectedImageView.setImageURI(null)
        selectedImageView.visibility = View.GONE
        addImageButton.visibility = View.VISIBLE
        deleteButton.visibility = View.GONE
    }

    private fun validateAndPost() {
        val caption = captionInput.text.toString().trim()
        when {
            imageUri == null -> showToast("Please select an image")
            caption.isEmpty() -> showToast("Please write a caption")
            else -> {
                // TODO: Implement actual post logic
                showToast("News posted successfully!")
                finish()
            }
        }
    }

    // Permission handling
    private fun checkCameraPermission(): Boolean {
        return if(ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
            false
        }
    }

    private fun checkStoragePermission(): Boolean {
        val permission = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        return if(ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission),
                GALLERY_PERMISSION_CODE
            )
            false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            CAMERA_PERMISSION_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    showToast("Camera permission required")
                }
            }
            GALLERY_PERMISSION_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                } else {
                    showToast("Storage permission required")
                }
            }
        }
    }

    private fun createImageUri(): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "News_${System.currentTimeMillis()}")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}