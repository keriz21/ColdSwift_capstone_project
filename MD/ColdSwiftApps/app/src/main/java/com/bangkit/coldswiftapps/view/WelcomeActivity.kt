package com.bangkit.coldswiftapps.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bangkit.coldswiftapps.R
import com.bangkit.coldswiftapps.databinding.ActivityWelcomeBinding
import com.bangkit.coldswiftapps.view.login.LoginActivity
import com.bangkit.coldswiftapps.view.login.LoginViewModel
import com.bangkit.coldswiftapps.view.main.MainActivity
import com.bangkit.coldswiftapps.view.register.RegisterActivity
import com.bangkit.coldswiftapps.view.verif.FaceVerifActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var viewModel : LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)


        binding.btnLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener{
            val intent = Intent(this, FaceVerifActivity::class.java)
            startActivity(intent)
        }

        if (viewModel.getSession().isLogin) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}