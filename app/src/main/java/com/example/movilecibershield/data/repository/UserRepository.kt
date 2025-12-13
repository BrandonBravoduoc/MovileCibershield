package com.example.movilecibershield.data.repository




import com.example.movilecibershield.data.model.user.ChangePassword
import com.example.movilecibershield.data.model.user.ContactCreateWithAddress
import com.example.movilecibershield.data.model.user.ContactResponse
import com.example.movilecibershield.data.model.user.ContactUpdateWithAddress
import com.example.movilecibershield.data.model.user.UserProfile
import com.example.movilecibershield.data.model.user.UserResponse
import com.example.movilecibershield.data.remote.api.product.UserApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository(
    private val api: UserApiService
) {

    suspend fun getMyProfile(): RepoResult<UserProfile> {
        return try {
            val response = api.getMyProfile()
            if (response.isSuccessful) {
                RepoResult(data = response.body())
            } else {
                RepoResult(error = response.errorBody()?.string())
            }
        } catch (e: Exception) {
            RepoResult(error = e.message)
        }
    }

    suspend fun createContact(dto: ContactCreateWithAddress): RepoResult<ContactResponse> {
        return try {
            val response = api.createContact(dto)
            if (response.isSuccessful) {
                RepoResult(data = response.body())
            } else RepoResult(error = response.errorBody()?.string())
        } catch (e: Exception) {
            RepoResult(error = e.message)
        }
    }

    suspend fun updateContact(dto: ContactUpdateWithAddress): RepoResult<ContactResponse> {
        return try {
            val response = api.updateContact(dto)
            if (response.isSuccessful) {
                RepoResult(data = response.body())
            } else RepoResult(error = response.errorBody()?.string())
        } catch (e: Exception) {
            RepoResult(error = e.message)
        }
    }

    suspend fun updateUser(
        newUserName: RequestBody?,
        newEmail: RequestBody?,
        imageUser: MultipartBody.Part?
    ): RepoResult<UserResponse> {
        return try {
            val response = api.updateCurrentUser(newUserName, newEmail, imageUser)
            if (response.isSuccessful) {
                RepoResult(data = response.body())
            } else RepoResult(error = response.errorBody()?.string())
        } catch (e: Exception) {
            RepoResult(error = e.message)
        }
    }

    suspend fun changePassword(dto: ChangePassword): RepoResult<String> {
        return try {
            val response = api.changePassword(dto)
            if (response.isSuccessful) {
                val msg = response.body()?.get("message") ?: "Contraseña actualizada"
                RepoResult(data = msg)
            } else RepoResult(error = response.errorBody()?.string())
        } catch (e: Exception) {
            RepoResult(error = e.message)
        }
    }

    suspend fun deleteUser(id: Long): RepoResult<Int> {
        return try {
            val response = api.deleteUser(id)
            if (response.isSuccessful) {
                RepoResult(data = 200)
            } else RepoResult(error = response.errorBody()?.string())
        } catch (e: Exception) {
            RepoResult(error = e.message)
        }
    }
}

