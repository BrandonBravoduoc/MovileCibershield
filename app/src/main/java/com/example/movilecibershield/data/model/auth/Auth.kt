package com.example.movilecibershield.data.model.auth

import com.example.movilecibershield.data.model.user.UserResponse

data class LoginRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val user: UserResponse
)
