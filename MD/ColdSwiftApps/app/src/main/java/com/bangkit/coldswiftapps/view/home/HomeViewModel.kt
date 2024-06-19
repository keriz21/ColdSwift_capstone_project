package com.bangkit.coldswiftapps.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.coldswiftapps.data.EventRepository
import com.bangkit.coldswiftapps.data.remote.response.ListEventResponse

class HomeViewModel(private val repository: EventRepository) : ViewModel() {
    private val _events = MutableLiveData<List<ListEventResponse>>()
    val events: LiveData<List<ListEventResponse>> = _events

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        _isLoading.value = false
    }

    fun getAllEvents() {
        _isLoading.value = true
        repository.getAllEvents { result ->
            _isLoading.postValue(false)
            result.onSuccess { eventsList ->
                _events.postValue(eventsList)
            }
            result.onFailure { exception ->
                // Handle failure (e.g., show error message)
//                Result.failure(exception)

            }
        }
    }
}