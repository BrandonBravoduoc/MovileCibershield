package com.example.movilecibershield.data.remote.api.product

import com.example.movilecibershield.data.model.user.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface UserApiService {

    @GET("users/profile")
    suspend fun getMyProfile(): Response<UserProfile>

    @POST("users/contact")
    suspend fun createContact(
        @Body dto: ContactCreateWithAddress
    ): Response<ContactResponse>

    @PATCH("users/update")
    suspend fun updateContact(
        @Body dto: ContactUpdateWithAddress
    ): Response<ContactResponse>

    @Multipart
    @PATCH("users/me")
    suspend fun updateCurrentUser(
        @Part("newUserName") newUserName: RequestBody?,
        @Part("newEmail") newEmail: RequestBody?,
        @Part imageUser: MultipartBody.Part?
    ): Response<UserResponse>

    @PATCH("users/me/change-password")
    suspend fun changePassword(
        @Body dto: ChangePassword
    ): Response<Map<String, String>>

    @DELETE("users/delete")
    suspend fun deleteUser(
        @Query("id") id: Long
    ): Response<Int>
}
