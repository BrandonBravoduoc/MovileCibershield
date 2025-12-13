package com.example.movilecibershield.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movilecibershield.data.model.user.*
import com.example.movilecibershield.data.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserViewModel(private val repo: UserRepository) : ViewModel() {

    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile: StateFlow<UserProfile?> = _profile

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadProfile(token: String?) {

        if (token == null) {
            _loading.value = false
            _error.value = null
            _profile.value = null
            return
        }

        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            val result = repo.getMyProfile()
            _loading.value = false

            result.data?.let { profile ->
                _profile.value = profile
            } ?: run {
                _error.value = result.error
            }
        }
    }


    fun createContact(dto: ContactCreateWithAddress, token: String?) {
        viewModelScope.launch {
            val result = repo.createContact(dto)

            if (result.data != null) {
                loadProfile(token)
            } else {
                _error.value = result.error
            }
        }
    }

    fun updateContact(dto: ContactUpdateWithAddress, token: String?) {
        viewModelScope.launch {
            val result = repo.updateContact(dto)

            if (result.data != null) {
                loadProfile(token)
            } else {
                _error.value = result.error
            }
        }
    }


    fun updateUser(
        userName: RequestBody?,
        email: RequestBody?,
        imageUser: MultipartBody.Part?,
        token: String?
    ) {
        viewModelScope.launch {
            val result = repo.updateUser(userName, email, imageUser)

            if (result.data != null) {
                loadProfile(token)
            } else {
                _error.value = result.error
            }
        }
    }

    fun changePassword(dto: ChangePassword) {
        viewModelScope.launch {
            val result = repo.changePassword(dto)
            if (result.data == null) {
                _error.value = result.error
            }
        }
    }

    fun deleteUser(id: Long) {
        viewModelScope.launch {
            val result = repo.deleteUser(id)
            if (result.error != null) {
                _error.value = result.error
            }
        }
    }
}



