package com.example.movilecibershield.data.repository


import com.example.movilecibershield.data.model.user.*
import com.example.movilecibershield.data.remote.api.product.UserApiService
import com.example.movilecibershield.data.utils.extractErrorMessage
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException

class UserRepository(
    private val api: UserApiService
) {

    suspend fun getMyProfile(): RepoResult<UserProfile> {
        return try {
            val response = api.getMyProfile()
            RepoResult(data = response.body())
        } catch (e: HttpException) {
            RepoResult(error = extractErrorMessage(e))
        } catch (e: IOException) {
            RepoResult(error = "Sin conexión a internet")
        } catch (e: Exception) {
            RepoResult(error = "Error inesperado al obtener el perfil")
        }
    }

    suspend fun createContact(dto: ContactCreateWithAddress): RepoResult<ContactResponse> {
        return try {
            val response = api.createContact(dto)
            RepoResult(data = response.body())
        } catch (e: HttpException) {
            RepoResult(error = extractErrorMessage(e))
        } catch (e: IOException) {
            RepoResult(error = "Sin conexión a internet")
        } catch (e: Exception) {
            RepoResult(error = "Error inesperado al crear el contacto")
        }
    }

    suspend fun updateContact(dto: ContactUpdateWithAddress): RepoResult<ContactResponse> {
        return try {
            val response = api.updateContact(dto)
            RepoResult(data = response.body())
        } catch (e: HttpException) {
            RepoResult(error = extractErrorMessage(e))
        } catch (e: IOException) {
            RepoResult(error = "Sin conexión a internet")
        } catch (e: Exception) {
            RepoResult(error = "Error inesperado al actualizar el contacto")
        }
    }

    suspend fun updateUser(
        newUserName: RequestBody?,
        newEmail: RequestBody?,
        imageUser: MultipartBody.Part?
    ): RepoResult<UserResponse> {
        return try {
            val response = api.updateCurrentUser(newUserName, newEmail, imageUser)
            RepoResult(data = response.body())
        } catch (e: HttpException) {
            RepoResult(error = extractErrorMessage(e))
        } catch (e: IOException) {
            RepoResult(error = "Sin conexión a internet")
        } catch (e: Exception) {
            RepoResult(error = "Error inesperado al actualizar el usuario")
        }
    }

    suspend fun changePassword(dto: ChangePassword): RepoResult<Map<String, String>> {
        return try {
            val response = api.changePassword(dto)
            RepoResult(data = response.body())
        } catch (e: HttpException) {
            RepoResult(error = extractErrorMessage(e))
        } catch (e: IOException) {
            RepoResult(error = "Sin conexión a internet")
        } catch (e: Exception) {
            RepoResult(error = "Error inesperado al cambiar la contraseña")
        }
    }

    suspend fun deleteUser(id: Long): RepoResult<Int> {
        return try {
            val response = api.deleteUser(id)
            RepoResult(data = response.body())
        } catch (e: HttpException) {
            RepoResult(error = extractErrorMessage(e))
        } catch (e: IOException) {
            RepoResult(error = "Sin conexión a internet")
        } catch (e: Exception) {
            RepoResult(error = "Error inesperado al eliminar el usuario")
        }
    }
}

