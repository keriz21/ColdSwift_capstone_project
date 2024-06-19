package com.bangkit.coldswiftapps.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.coldswiftapps.data.EventRepository
import com.bangkit.coldswiftapps.data.preference.UserModel
import com.bangkit.coldswiftapps.data.remote.response.LoginResponse
import com.bangkit.coldswiftapps.view.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginViewModel(private val repository: EventRepository): ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {
        _isLoading.value = true
        repository.loginUser(email, password).observeForever { result ->
            _isLoading.value = false
            _loginResult.value = result
            ViewModelFactory.resetInstance()
        }
    }

    fun getSession() = runBlocking {
        repository.getSession().first()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logoutUser()

            val session = repository.getSession().first()
            Log.d("MainViewModel", "Preferences after logout: $session")
        }
    }

}