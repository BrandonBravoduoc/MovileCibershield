package com.example.movilecibershield.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movilecibershield.data.local.TokenDataStore
import com.example.movilecibershield.data.model.auth.AuthResponse
import com.example.movilecibershield.data.model.user.UserRegister
import com.example.movilecibershield.data.repository.AuthRepository
import com.example.movilecibershield.ui.screens.auth.AuthMode
import kotlinx.coroutines.launch


class AuthViewModel(
    private val authRepository: AuthRepository,
    private val tokenDataStore: TokenDataStore
) : ViewModel() {

    // Estado
    var authMode by mutableStateOf(AuthMode.LOGIN)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var authResponse by mutableStateOf<AuthResponse?>(null)
        private set

    // Cambiar al modo LOGIN
    fun switchToLogin() {
        authMode = AuthMode.LOGIN
        clearError()
    }

    // Cambiar al modo REGISTER
    fun switchToRegister() {
        authMode = AuthMode.REGISTER
        clearError()
    }

    // Login
    fun login(email: String, password: String) {
        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            val result = authRepository.login(email, password)

            isLoading = false
            result.data?.let {
                authResponse = it
                tokenDataStore.saveToken(it.token)  // Guardar token
            }
            result.error?.let {
                errorMessage = it
            }
        }
    }

    // Register (y auto-login)
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
                userRegister.email,
                userRegister.password
            )

            isLoading = false

            loginResult.data?.let {
                authResponse = it
                tokenDataStore.saveToken(it.token)  // Guardar token después de registrar
            }

            loginResult.error?.let {
                errorMessage = it
            }
        }
    }

    // Limpiar errores
    fun clearError() {
        errorMessage = null
    }

    // Limpiar resultados
    fun clearResults() {
        authResponse = null
    }
}


