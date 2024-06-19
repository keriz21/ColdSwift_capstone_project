package com.bangkit.coldswiftapps.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bangkit.coldswiftapps.databinding.ActivityLoginBinding
import com.bangkit.coldswiftapps.view.ViewModelFactory
import com.bangkit.coldswiftapps.view.main.MainActivity
import com.bangkit.coldswiftapps.view.register.RegisterActivity
import com.bangkit.coldswiftapps.view.verif.FaceVerifActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        loginViewModel.isLoading.observe(this, Observer { isLoading ->
            showLoading(isLoading)
        })

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPass.getText().toString()
            loginViewModel.login(email, password)
        }

        binding.btnLogin.doOnTextChanged { text, start, before, count ->
            if(text!!.length > 8){
                binding.textInputLayout.error = "Password must be at least 8 characters"
            } else {
                binding.textInputLayout.error = null
            }
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, FaceVerifActivity::class.java)
            startActivity(intent)
        }

        loginViewModel.loginResult.observe(this, Observer { result ->
            result?.onSuccess { loginResponse ->
                Log.d("LoginActivity", "Login successful, navigating to MainActivity")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            result?.onFailure { exception ->
                Log.e("LoginActivity", "Login failed: ${exception.message}")
                Toast.makeText(this, "Login failed: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
