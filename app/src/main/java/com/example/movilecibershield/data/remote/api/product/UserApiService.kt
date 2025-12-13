package com.example.movilecibershield.data.remote.api.product

import com.example.movilecibershield.data.model.user.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface UserApiService {

    @GET("api/v1/users/profile")
    suspend fun getMyProfile(): Response<UserProfile>

    @POST("api/v1/users/contact")
    suspend fun createContact(
        @Body dto: ContactCreateWithAddress
    ): Response<ContactResponse>

    @PATCH("api/v1/users/update")
    suspend fun updateContact(
        @Body dto: ContactUpdateWithAddress
    ): Response<ContactResponse>

    @Multipart
    @PATCH("api/v1/users/me")
    suspend fun updateCurrentUser(
        @Part("newUserName") newUserName: RequestBody?,
        @Part("newEmail") newEmail: RequestBody?,
        @Part imageUser: MultipartBody.Part?
    ): Response<UserResponse>

    @PATCH("api/v1/users/me/change-password")
    suspend fun changePassword(
        @Body dto: ChangePassword
    ): Response<Map<String, String>>

    @DELETE("api/v1/users/delete")
    suspend fun deleteUser(
        @Query("id") id: Long
    ): Response<Int>
}
