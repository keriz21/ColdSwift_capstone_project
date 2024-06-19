package com.bangkit.coldswiftapps.view.verif

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.coldswiftapps.R
import com.bangkit.coldswiftapps.databinding.ActivityKtpverifBinding
import com.bangkit.coldswiftapps.view.camera.CameraActivity
import com.bangkit.coldswiftapps.view.register.RegisterActivity

class KTPVerifActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKtpverifBinding
    private var currentImageUri: Uri? = null
    private var faceImageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKtpverifBinding.inflate(layoutInflater)
        setContentView(binding.root)

        faceImageUri = intent.getStringExtra(FACE_URI)?.toUri()

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(KTPVerifActivity.REQUIRED_PERMISSION)
        }


        binding.btnUpload.setOnClickListener {
            startCamera()
        }

        binding.btnBack.setOnClickListener{
            finish()
        }

        binding.btnRepeat.setOnClickListener {
            clearImage()
        }

        binding.btnGallery.setOnClickListener {
            startGallery()
        }

        binding.btnConfirm.setOnClickListener{
            currentImageUri?.let { uri ->
                val intent = Intent(this, RegisterActivity::class.java).apply {
                    putExtra(RegisterActivity.EXTRA_PHOTO, faceImageUri.toString())
                    putExtra(RegisterActivity.EXTRA_KTP, uri.toString())
                }
                startActivity(intent)
            }
        }
    }

    private fun clearImage() {
        currentImageUri = null
        binding.ivImage.setImageURI(null)
        binding.asset.visibility = View.VISIBLE
        binding.btnRepeat.visibility = View.GONE
    }

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CameraActivity.CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }



    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivImage.setImageURI(it)
        }

        if (currentImageUri != null) {
            binding.asset.visibility = View.GONE
            binding.btnRepeat.visibility = View.VISIBLE

        }
    }


    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
        const val FACE_URI = "face_uri"
    }
}