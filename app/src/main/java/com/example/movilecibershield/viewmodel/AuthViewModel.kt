package com.example.movilecibershield.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movilecibershield.data.local.TokenDataStore
import com.example.movilecibershield.data.model.auth.AuthResponse
import com.example.movilecibershield.data.model.auth.LoginRequest
import com.example.movilecibershield.data.model.user.UserRegister
import com.example.movilecibershield.data.repository.AuthRepository
import com.example.movilecibershield.data.utils.UiEvent
import com.example.movilecibershield.ui.screens.auth.AuthMode
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val tokenDataStore: TokenDataStore
) : ViewModel() {

    var authMode by mutableStateOf(AuthMode.LOGIN)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var authResponse by mutableStateOf<AuthResponse?>(null)
        private set

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent


    fun switchToLogin() {
        authMode = AuthMode.LOGIN
    }

    fun switchToRegister() {
        authMode = AuthMode.REGISTER
    }

    fun login(loginRequest: LoginRequest) {
        isLoading = true

        viewModelScope.launch {
            val result = authRepository.login(loginRequest)
            isLoading = false

            result.data?.let {
                authResponse = it
                tokenDataStore.saveToken(it.token)
                tokenDataStore.saveUserId(it.user.id)
            }

            result.error?.let {
                _uiEvent.emit(UiEvent.ShowSnackbar(it))
            }
        }
    }

    fun register(userRegister: UserRegister) {
        isLoading = true

        viewModelScope.launch {
            val registerResult = authRepository.register(userRegister)

            if (registerResult.error != null) {
                isLoading = false
                _uiEvent.emit(UiEvent.ShowSnackbar(registerResult.error))
                return@launch
            }

            val loginResult = authRepository.login(
                LoginRequest(
                    email = userRegister.email,
                    password = userRegister.password
                )
            )

            isLoading = false

            loginResult.data?.let {
                authResponse = it
                tokenDataStore.saveToken(it.token)
                tokenDataStore.saveUserId(it.user.id)
            }

            loginResult.error?.let {
                _uiEvent.emit(UiEvent.ShowSnackbar(it))
            }
        }
    }

    fun clearResults() {
        authResponse = null
    }
}
