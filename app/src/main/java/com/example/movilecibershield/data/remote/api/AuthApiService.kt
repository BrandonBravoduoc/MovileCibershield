package com.example.movilecibershield.data.remote.api

import com.example.movilecibershield.data.model.auth.AuthResponse
import com.example.movilecibershield.data.model.auth.LoginRequest
import com.example.movilecibershield.data.model.user.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthApiService {

    @POST("api/v1/auth/signin")
    suspend fun login(
        @Body request: LoginRequest
    ): AuthResponse

    @Multipart
    @POST("api/v1/auth/register")
    suspend fun register(
        @Part("userName") userName: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("confirmPassword") confirmPassword: RequestBody,
        @Part imageUser: MultipartBody.Part?
    ): UserResponse


}