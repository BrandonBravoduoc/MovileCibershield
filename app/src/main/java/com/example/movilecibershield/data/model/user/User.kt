package com.example.movilecibershield.data.model.user

data class UserRegister(
    val userName: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
    val imageUser: String?
)

data class UserResponse(
    val id: Long,
    val userName: String,
    val email: String,
    val imageUser: String?,
    val nameRole: String
)

data class UserUpdate(
    val newUserName: String,
    val newEmail: String
)

data class ChangePassword(
    val currentPassword: String,
    val newPassword: String,
    val confirmPassword: String
)

data class UserProfile(
    val userName: String,
    val email: String,
    val imageUser: String?,
    val contact: ContactResponse?
)
