package com.example.movilecibershield.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movilecibershield.data.model.order.OrderResponse
import com.example.movilecibershield.data.model.user.*
import com.example.movilecibershield.data.repository.OrderRepository
import com.example.movilecibershield.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserViewModel(
    private val repo: UserRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile: StateFlow<UserProfile?> = _profile

    private val _orders = MutableStateFlow<List<OrderResponse>>(emptyList())
    val orders: StateFlow<List<OrderResponse>> = _orders

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadProfile() {
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            val result = repo.getMyProfile()
            _loading.value = false

            result.data?.let {
                _profile.value = it
            } ?: run {
                _error.value = result.error
            }
        }
    }

    fun loadOrders() {
        viewModelScope.launch {
            val result = orderRepository.getAllOrders()
            result.data?.let {
                _orders.value = it
            } ?: run {
                _error.value = result.error
            }
        }
    }

    fun createContact(dto: ContactCreateWithAddress) {
        viewModelScope.launch {
            val result = repo.createContact(dto)
            if (result.data != null) {
                loadProfile()
            } else {
                _error.value = result.error
            }
        }
    }

    fun updateContact(dto: ContactUpdateWithAddress) {
        viewModelScope.launch {
            val result = repo.updateContact(dto)
            if (result.data != null) {
                loadProfile()
            } else {
                _error.value = result.error
            }
        }
    }

    fun updateUser(
        userName: RequestBody?,
        email: RequestBody?,
        imageUser: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            val result = repo.updateUser(userName, email, imageUser)
            if (result.data != null) {
                loadProfile()
            } else {
                _error.value = result.error
            }
        }
    }

    fun changePassword(dto: ChangePassword) {
        viewModelScope.launch {
            val result = repo.changePassword(dto)
            if (result.data == null) {
                _error.value = result.error
            }
        }
    }

    fun deleteUser(id: Long) {
        viewModelScope.launch {
            val result = repo.deleteUser(id)
            if (result.error != null) {
                _error.value = result.error
            }
        }
    }
}
