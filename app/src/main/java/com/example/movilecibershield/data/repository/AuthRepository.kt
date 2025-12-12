package com.example.movilecibershield.data.repository

import android.R.id.message
import com.example.movilecibershield.data.local.TokenCache
import com.example.movilecibershield.data.model.auth.AuthResponse
import com.example.movilecibershield.data.model.auth.LoginRequest
import com.example.movilecibershield.data.model.user.UserResponse
import com.example.movilecibershield.data.remote.api.AuthApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException



class AuthRepository(
    private val authApiService: AuthApiService
) {

    suspend fun login(
        email: String,
        password: String
    ): RepoResult<AuthResponse> = withContext(Dispatchers.IO) {

        try {
            val response = authApiService.login(
                LoginRequest(
                    email = email,
                    password = password
                )
            )
            TokenCache.token = response.token
            RepoResult(data = response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()

            val message = if (!errorBody.isNullOrBlank()) {
                try {
                    JSONObject(errorBody).optString("error", "Error del servidor")
                } catch (_: Exception) {
                    "Error del servidor"
                }
            } else {
                "Error del servidor"
            }
            RepoResult(error = message)
        } catch (e: IOException) {
            RepoResult(error = "Sin conexión a internet")

        } catch (_: Exception) {
            RepoResult(error = "Error inesperado")
        }
    }

    suspend fun register(
        userName: String,
        email: String,
        password: String,
        confirmPassword: String,
        imagePart: MultipartBody.Part?
    ): RepoResult<UserResponse> = withContext(Dispatchers.IO){

        try {
            val response = authApiService.register(
                userName = userName.toRequestBody("text/plain".toMediaTypeOrNull()),
                email = email.toRequestBody("text/plain".toMediaTypeOrNull()),
                password = password.toRequestBody("text/plain".toMediaTypeOrNull()),
                confirmPassword = confirmPassword.toRequestBody("text/plain".toMediaTypeOrNull()),
                imageUser = imagePart
            )
            RepoResult(data = response)

        }catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()

            val message = if (!errorBody.isNullOrBlank()) {
                try {
                    JSONObject(errorBody).optString("error", "Error del servidor")
                } catch (_: Exception) {
                    "Error del servidor"
                }
            } else {
                "Error del servidor"
            }

            RepoResult(error = message)
        }catch (e: IOException){
            RepoResult(error = "Sin conexión a internet")

        }catch (_: Exception){
            RepoResult(error = "Error inesperado")

        }
    }


}