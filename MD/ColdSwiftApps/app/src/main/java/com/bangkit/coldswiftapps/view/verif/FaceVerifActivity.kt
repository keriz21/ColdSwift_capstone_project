package com.bangkit.coldswiftapps.view.verif

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.coldswiftapps.R
import com.bangkit.coldswiftapps.databinding.ActivityFaceVerifBinding
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bangkit.coldswiftapps.view.camera.CameraActivity
import com.bangkit.coldswiftapps.view.camera.CameraActivity.Companion.CAMERAX_RESULT

class FaceVerifActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFaceVerifBinding
    private var currentImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFaceVerifBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
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

        binding.btnConfirm.setOnClickListener{
            currentImageUri?.let { uri ->
                val intent = Intent(this, KTPVerifActivity::class.java).apply {
                    putExtra(KTPVerifActivity.FACE_URI, uri.toString())
                }
                startActivity(intent)
            }
        }

        binding.btnGallery.setOnClickListener {
            startGallery()
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
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
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

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}