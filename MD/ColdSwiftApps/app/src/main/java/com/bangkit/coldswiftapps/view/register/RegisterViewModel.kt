package com.bangkit.coldswiftapps.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.coldswiftapps.data.EventRepository
import com.bangkit.coldswiftapps.data.remote.response.RegisterResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RegisterViewModel(private val repository: EventRepository): ViewModel() {
    private val _registerResult = MutableLiveData<Result<RegisterResponse>>()
    val registerResult: LiveData<Result<RegisterResponse>> = _registerResult

    fun register (ktp: MultipartBody.Part, photo: MultipartBody.Part, email: RequestBody, password: RequestBody){
        viewModelScope.launch {
            _registerResult.value = repository.register(ktp, photo, email, password)
        }
    }
}