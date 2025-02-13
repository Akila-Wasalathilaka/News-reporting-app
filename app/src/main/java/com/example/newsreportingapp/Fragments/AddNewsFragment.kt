package com.example.newsreportingapp.Fragments

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat
import com.example.newsreportingapp.R

class AddNewsFragment : Fragment(R.layout.add_news) {
    private lateinit var selectedImageView: ImageView
    private lateinit var addImageButton: ImageButton
    private lateinit var deleteButton: ImageButton
    private lateinit var sendButton: Button
    private lateinit var captionInput: EditText
    private lateinit var capturePhotoButton: Button

    private var imageUri: Uri? = null

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                imageUri = uri
                loadImageToView(uri)
            }
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                imageUri?.let { loadImageToView(it) }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedImageView = view.findViewById(R.id.selectedImageView)
        addImageButton = view.findViewById(R.id.addImageButton)
        deleteButton = view.findViewById(R.id.deleteButton)
        sendButton = view.findViewById(R.id.sendButton)
        captionInput = view.findViewById(R.id.captionInput)
        capturePhotoButton = view.findViewById(R.id.capturePhotoButton)

        addImageButton.setOnClickListener { openGallery() }
        capturePhotoButton.setOnClickListener { openCamera() }
        deleteButton.setOnClickListener { clearImageSelection() }
        sendButton.setOnClickListener { validateAndPost() }
    }

    private fun openGallery() {
        if (checkStoragePermission()) {
            galleryLauncher.launch("image/*")
        }
    }

    private fun openCamera() {
        if (checkCameraPermission()) {
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
                showToast("News posted successfully!")
                clearImageSelection()
                captionInput.text.clear()
            }
        }
    }

    private fun checkCameraPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
            false
        }
    }

    private fun checkStoragePermission(): Boolean {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        return if (ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            requestPermissions(arrayOf(permission), 101)
            false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    showToast("Camera permission required")
                }
            }
            101 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
        return requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
