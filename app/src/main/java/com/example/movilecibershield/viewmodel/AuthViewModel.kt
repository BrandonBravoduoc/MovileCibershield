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
import com.example.movilecibershield.ui.screens.auth.AuthMode
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val tokenDataStore: TokenDataStore
) : ViewModel() {

    var authMode by mutableStateOf(AuthMode.LOGIN)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var authResponse by mutableStateOf<AuthResponse?>(null)
        private set

    fun switchToLogin() {
        authMode = AuthMode.LOGIN
        clearError()
    }

    fun switchToRegister() {
        authMode = AuthMode.REGISTER
        clearError()
    }

    fun login(loginRequest: LoginRequest) {
        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            val result = authRepository.login(loginRequest)

            isLoading = false
            result.data?.let {
                authResponse = it
                tokenDataStore.saveToken(it.token)
            }
            result.error?.let {
                errorMessage = it
            }
        }
    }

    fun register(userRegister: UserRegister) {
        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            val registerResult = authRepository.register(userRegister)

            registerResult.error?.let {
                isLoading = false
                errorMessage = it
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
            }

            loginResult.error?.let {
                errorMessage = it
            }
        }
    }

    fun clearError() {
        errorMessage = null
    }

    fun clearResults() {
        authResponse = null
    }
}