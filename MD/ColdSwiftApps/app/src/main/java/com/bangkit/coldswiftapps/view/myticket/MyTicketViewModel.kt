package com.bangkit.coldswiftapps.view.myticket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.coldswiftapps.data.EventRepository
import com.bangkit.coldswiftapps.data.remote.response.AllTicketResponse
import com.bangkit.coldswiftapps.data.remote.response.AllTicketResponseItem
import com.bangkit.coldswiftapps.data.remote.response.ListEventResponse
import kotlinx.coroutines.launch

class MyTicketViewModel(private val repository: EventRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _allTickets = MutableLiveData<List<AllTicketResponseItem>>()
    val allTickets: LiveData<List<AllTicketResponseItem>> = _allTickets

    fun getAllTickets() {
        _isLoading.value = true
        repository.getAllTickets { result ->
            _isLoading.value = false
            result.onSuccess { tickets ->
                _allTickets.value = tickets
            }
            result.onFailure { exception ->
//                showErrorToast(exception.message ?: "Unknown error")
            }
        }
    }

}