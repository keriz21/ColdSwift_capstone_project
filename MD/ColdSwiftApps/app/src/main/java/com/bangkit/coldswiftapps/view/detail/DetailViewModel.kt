package com.bangkit.coldswiftapps.view.detail

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.coldswiftapps.data.EventRepository
import com.bangkit.coldswiftapps.data.remote.response.BuyTiketResponse
import com.bangkit.coldswiftapps.data.remote.response.DetailEventResponse
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: EventRepository) : ViewModel() {
    private val _eventDetail = MutableLiveData<Result<DetailEventResponse>>()
    val eventDetail: LiveData<Result<DetailEventResponse>> = _eventDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _buyTiket = MutableLiveData<Result<BuyTiketResponse>>()
    val buyTiket: LiveData<Result<BuyTiketResponse>> = _buyTiket

    fun getDetailEvent(id: String){
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getDetailEvent(id)
            _eventDetail.value = result
            _isLoading.value = false
        }
    }

    fun purchaseTiket(id: String){
        viewModelScope.launch {
            val result = repository.purchaseTicket(id)
            _buyTiket.value = result
        }

    }

}