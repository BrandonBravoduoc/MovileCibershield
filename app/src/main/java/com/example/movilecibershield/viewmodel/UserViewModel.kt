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
import com.example.movilecibershield.data.utils.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserViewModel(
    private val repo: UserRepository,
    private val orderRepository: OrderRepository,
    private val tokenDataStore: TokenDataStore
) : ViewModel() {


    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile: StateFlow<UserProfile?> = _profile.asStateFlow()

    private val _orders = MutableStateFlow<List<OrderResponse>>(emptyList())
    val orders: StateFlow<List<OrderResponse>> = _orders.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()


    private val _uiEvent = Channel<UiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()


    fun loadProfile() {
        viewModelScope.launch {
            _loading.value = true

            val result = repo.getMyProfile()
            result.data?.let { profile ->
                _profile.value = profile

                val userId = tokenDataStore.getUserId().first()
                if (userId != null) {
                    loadOrders(userId)
                }
            } ?: run {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(result.error ?: "Error al cargar el perfil")
                )
            }

            _loading.value = false
        }
    }


    private fun loadOrders(userId: Long) {
        viewModelScope.launch {
            val ordersResult = orderRepository.getAllOrders()
            ordersResult.data?.let { allOrders ->
                _orders.value = allOrders.filter { it.user.id == userId }
            } ?: run {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(ordersResult.error ?: "Error al cargar órdenes")
                )
            }
        }
    }

    fun refreshOrders() {
        viewModelScope.launch {
            _loading.value = true

            val userId = tokenDataStore.getUserId().first()
            if (userId == null) {
                _uiEvent.send(UiEvent.ShowSnackbar("No se pudo identificar al usuario"))
                _loading.value = false
                return@launch
            }

            val ordersResult = orderRepository.getAllOrders()
            ordersResult.data?.let {
                _orders.value = it.filter { order -> order.user.id == userId }
            } ?: run {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(ordersResult.error ?: "Error al cargar órdenes")
                )
            }

            _loading.value = false
        }
    }


    fun createContact(dto: ContactCreateWithAddress) {
        viewModelScope.launch {
            val result = repo.createContact(dto)
            if (result.data != null) {
                loadProfile()
            } else {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(result.error ?: "Error al crear contacto")
                )
            }
        }
    }

    fun updateContact(dto: ContactUpdateWithAddress) {
        viewModelScope.launch {
            val result = repo.updateContact(dto)
            if (result.data != null) {
                loadProfile()
            } else {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(result.error ?: "Error al actualizar contacto")
                )
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
                _uiEvent.send(
                    UiEvent.ShowSnackbar(result.error ?: "Error al actualizar usuario")
                )
            }
        }
    }

    fun changePassword(dto: ChangePassword) {
        viewModelScope.launch {
            val result = repo.changePassword(dto)
            if (result.data == null) {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(result.error ?: "Error al cambiar contraseña")
                )
            }
        }
    }

    fun deleteUser(id: Long) {
        viewModelScope.launch {
            val result = repo.deleteUser(id)
            if (result.error != null) {
                _uiEvent.send(UiEvent.ShowSnackbar(result.error))
            }
        }
    }
}
