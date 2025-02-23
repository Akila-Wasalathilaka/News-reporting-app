package com.example.newsreportingapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.newsreportingapp.databinding.AddNewsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*

class AddNewsFragment : Fragment(R.layout.add_news) {
    private companion object {
        const val TAG = "AddNewsFragment"
        const val MAX_IMAGE_SIZE = 100 * 1024 // 100 KB
        const val COMPRESSION_QUALITY = 80 // JPEG compression quality
    }

    private var _binding: AddNewsBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null
    private var base64Image: String? = null
    private var isUploading = false

    // Firebase Firestore instance
    private val firestore = FirebaseFirestore.getInstance()

    // Image picker for selecting an image
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            imageUri = it
            binding.selectedImageView.setImageURI(it)
            lifecycleScope.launch {
                val result = convertUriToBase64(it)
                if (result != null) {
                    base64Image = result
                } else {
                    binding.selectedImageView.setImageResource(R.drawable.error_img)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = AddNewsBinding.bind(view)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.sendButton.setOnClickListener {
            if (!isUploading && validateInputs()) {
                uploadPostToFirestore()
            }
        }

        binding.addImageButton.setOnClickListener {
            pickImageFromGallery()
        }

        binding.deleteButton.setOnClickListener {
            resetFields()
        }
    }

    private fun validateInputs(): Boolean {
        return when {
            binding.topicInput.text.isNullOrEmpty() -> {
                showError("Please enter a topic")
                false
            }
            binding.captionInput.text.isNullOrEmpty() -> {
                showError("Please enter a caption")
                false
            }
            base64Image == null -> {
                showError("Please select a valid image")
                false
            }
            else -> true
        }
    }

    private fun uploadPostToFirestore() {
        isUploading = true
        setLoadingState(true)

        lifecycleScope.launch {
            try {
                val currentUser = FirebaseAuth.getInstance().currentUser
                val username = currentUser?.displayName ?: "Anonymous"

                val post = hashMapOf(
                    "postId" to UUID.randomUUID().toString(),
                    "topic" to binding.topicInput.text.toString().trim(),
                    "caption" to binding.captionInput.text.toString().trim(),
                    "image" to base64Image!!,
                    "reporter" to username,
                    "timestamp" to System.currentTimeMillis()
                )

                // Upload post to Firestore
                val response = withContext(Dispatchers.IO) {
                    firestore.collection("posts").add(post)
                }

                withContext(Dispatchers.Main) {
                    handleUploadSuccess()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Upload failed", e)
                showError("Upload failed: ${e.message}")
            } finally {
                isUploading = false
                setLoadingState(false)
            }
        }
    }

    private suspend fun convertUriToBase64(uri: Uri): String? {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()

                if (bitmap == null) return@withContext null

                val outputStream = ByteArrayOutputStream()
                var quality = COMPRESSION_QUALITY
                var byteArray: ByteArray

                // Loop to compress the image until it meets the max size requirement
                do {
                    outputStream.reset()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                    byteArray = outputStream.toByteArray()
                    quality -= 10
                } while (byteArray.size > MAX_IMAGE_SIZE && quality > 50)

                Base64.encodeToString(byteArray, Base64.DEFAULT)
            } catch (e: Exception) {
                Log.e(TAG, "Image conversion failed", e)
                null
            }
        }
    }

    private fun handleUploadSuccess() {
        Toast.makeText(requireContext(), "Post uploaded successfully", Toast.LENGTH_LONG).show()
        resetFields()
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun setLoadingState(loading: Boolean) {
        binding.apply {
            progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            sendButton.isEnabled = !loading
            addImageButton.isEnabled = !loading
            deleteButton.isEnabled = !loading
            topicInput.isEnabled = !loading
            captionInput.isEnabled = !loading
        }
    }

    private fun pickImageFromGallery() {
        imagePickerLauncher.launch("image/*")
    }

    private fun resetFields() {
        binding.apply {
            topicInput.text?.clear()
            captionInput.text?.clear()
            selectedImageView.setImageResource(0)
            imageUri = null
            base64Image = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
