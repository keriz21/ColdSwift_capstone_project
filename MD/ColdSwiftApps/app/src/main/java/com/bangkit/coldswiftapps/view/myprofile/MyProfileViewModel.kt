package com.bangkit.coldswiftapps.view.myprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.coldswiftapps.data.EventRepository
import com.bangkit.coldswiftapps.data.remote.response.ProfileResponse
import kotlinx.coroutines.launch

class MyProfileViewModel(private val repository: EventRepository) : ViewModel() {

    private val _profile = MutableLiveData<ProfileResponse>()
    val profile: LiveData<ProfileResponse> get() = _profile

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun getProfile(){
        viewModelScope.launch {
            try {
                _profile.value = repository.getProfile()
            } catch (e: Exception){
                _error.value = e.message

            }
        }
    }

}