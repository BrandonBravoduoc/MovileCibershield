package com.example.movilecibershield.data.repository

import com.example.movilecibershield.data.local.TokenCache
import com.example.movilecibershield.data.model.auth.AuthResponse
import com.example.movilecibershield.data.model.auth.LoginRequest
import com.example.movilecibershield.data.model.user.UserRegister
import com.example.movilecibershield.data.model.user.UserResponse
import com.example.movilecibershield.data.remote.api.AuthApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.IOException

class AuthRepository(
    private val authApiService: AuthApiService
) {

    suspend fun login(loginRequest: LoginRequest): RepoResult<AuthResponse> {
        return try {
            val response = authApiService.login(loginRequest)
            TokenCache.token = response.token
            RepoResult(data = response)
        } catch (e: HttpException) {
            RepoResult(error = e.message())
        } catch (e: IOException) {
            RepoResult(error = "Sin conexión a internet")
        } catch (e: Exception) {
            RepoResult(error = "Error inesperado")
        }
    }

    suspend fun register(
        user: UserRegister,
        imagePart: MultipartBody.Part? = null
    ): RepoResult<UserResponse> {
        return try {
            val response = authApiService.register(
                userName = user.userName.toRequestBody("text/plain".toMediaTypeOrNull()),
                email = user.email.toRequestBody("text/plain".toMediaTypeOrNull()),
                password = user.password.toRequestBody("text/plain".toMediaTypeOrNull()),
                confirmPassword = user.confirmPassword.toRequestBody("text/plain".toMediaTypeOrNull()),
                imageUser = imagePart
            )
            RepoResult(data = response)
        } catch (e: HttpException) {
            RepoResult(error = e.message())
        } catch (e: IOException) {
            RepoResult(error = "Sin conexión a internet")
        } catch (e: Exception) {
            RepoResult(error = "Error inesperado")
        }
    }
}
