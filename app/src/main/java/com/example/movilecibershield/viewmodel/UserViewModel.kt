package com.example.movilecibershield.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movilecibershield.data.local.TokenDataStore
import com.example.movilecibershield.data.model.order.OrderResponse
import com.example.movilecibershield.data.model.user.ChangePassword
import com.example.movilecibershield.data.model.user.ContactCreateWithAddress
import com.example.movilecibershield.data.model.user.ContactUpdateWithAddress
import com.example.movilecibershield.data.model.user.UserProfile
import com.example.movilecibershield.data.repository.OrderRepository
import com.example.movilecibershield.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class  UserViewModel(
    private val repo: UserRepository,
    private val orderRepository: OrderRepository,
    private val tokenDataStore: TokenDataStore
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
        viewModelScope.launch {
            _loading.value = true
            try {
                val profileResult = repo.getMyProfile()
                profileResult.data?.let {
                    _profile.value = it
                    val userId = tokenDataStore.getUserId().first()
                    if (userId != null) {
                        loadOrders(userId)
                    } else {
                        _error.value = "No se pudo verificar el ID del usuario para cargar las órdenes."
                    }
                } ?: run {
                    _error.value = profileResult.error
                }
            } finally {
                _loading.value = false
            }
        }
    }


    fun refreshOrders() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val userId = tokenDataStore.getUserId().first()
                if (userId != null) {
                    val ordersResult = orderRepository.getAllOrders()
                    ordersResult.data?.let { allOrders ->
                        _orders.value = allOrders.filter { order -> order.user.id == userId }
                    } ?: run {
                        _error.value = ordersResult.error
                    }
                } else {
                    _error.value = "No se pudo identificar al usuario."
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    private fun loadOrders(userId: Long) {
        viewModelScope.launch {
            val ordersResult = orderRepository.getAllOrders()
            ordersResult.data?.let {
                _orders.value = it.filter { order -> order.user.id == userId }
            } ?: run {
                _error.value = ordersResult.error
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
        newUserName: RequestBody?,
        newEmail: RequestBody?,
        imageUser: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            val result = repo.updateUser(newUserName, newEmail, imageUser)
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
