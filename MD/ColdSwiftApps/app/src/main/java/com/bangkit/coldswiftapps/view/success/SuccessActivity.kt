package com.bangkit.coldswiftapps.view.success

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.coldswiftapps.R
import com.bangkit.coldswiftapps.databinding.ActivityRegisterBinding
import com.bangkit.coldswiftapps.databinding.ActivitySuccessBinding
import com.bangkit.coldswiftapps.view.login.LoginActivity

class SuccessActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySuccessBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("userName")
        val userNik = intent.getStringExtra("NIK")
        val userEmail = intent.getStringExtra("Email")

        binding.tvName.text = userName
        binding.tvNik.text = userNik
        binding.tvEmail.text = userEmail

        binding.btnNext.setOnClickListener {
            val intent = Intent(this@SuccessActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}