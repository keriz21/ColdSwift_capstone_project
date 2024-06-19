package com.bangkit.coldswiftapps.view.register

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bangkit.coldswiftapps.R
import com.bangkit.coldswiftapps.databinding.ActivityRegisterBinding
import com.bangkit.coldswiftapps.utils.reduceFileImage
import com.bangkit.coldswiftapps.utils.uriToFile
import com.bangkit.coldswiftapps.view.ViewModelFactory
import com.bangkit.coldswiftapps.view.login.LoginActivity
import com.bangkit.coldswiftapps.view.main.MainActivity
import com.bangkit.coldswiftapps.view.success.SuccessActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Multipart

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    private var ktpImageUri: Uri? = null
    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]


        ktpImageUri = intent.getStringExtra(EXTRA_KTP)?.toUri()
        photoUri = intent.getStringExtra(EXTRA_PHOTO)?.toUri()

        binding.ivKtpVerification.setImageURI(ktpImageUri)
        binding.ivFaceVerification.setImageURI(photoUri)

        viewModel.registerResult.observe(this) { result ->
            showLoading(false)
            result.onSuccess {
                setResult(RESULT_OK)
                val intent = Intent(this, SuccessActivity::class.java)
                intent.putExtra("userName", it.name)
                intent.putExtra("NIK", it.nik)
                intent.putExtra("Email", it.email)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }.onFailure {
                showError(it.message.toString())
                showToast(it.message ?: getString(R.string.register_failed))
            }
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val pass = binding.edPass.text.toString()

            if (email.isEmpty() || pass.isEmpty()) {
                showToast("Email and Password must not be empty")
            } else {
                showLoading(true)
                register()
            }
        }

    }

    private fun register() {
        val ktpFile = uriToFile(ktpImageUri!!, this).reduceFileImage()
        val reqKTP = ktpFile.asRequestBody("image/jpeg".toMediaType())
        val faceFile = uriToFile(photoUri!!, this).reduceFileImage()
        val reqFace = faceFile.asRequestBody("image/jpeg".toMediaType())
        val email = binding.edLoginEmail.text.toString().toRequestBody("text/plain".toMediaType())
        val pass = binding.edPass.text.toString().toRequestBody("text/plain".toMediaType())

        val multipartKTP = MultipartBody.Part.createFormData("ktp_image", ktpFile.name, reqKTP)
        val multipartFace = MultipartBody.Part.createFormData("face_image", faceFile.name, reqFace)

        viewModel.register(multipartKTP, multipartFace, email, pass)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.failed_confirmation, null)
        val btnCancel = dialogView.findViewById<View>(R.id.btn_next)
        val tvMessage = dialogView.findViewById<TextView>(R.id.error_message)

        tvMessage.text = message

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()



        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    companion object {
        const val EXTRA_KTP = "extra_ktp"
        const val EXTRA_PHOTO = "extra_photo"
    }
}