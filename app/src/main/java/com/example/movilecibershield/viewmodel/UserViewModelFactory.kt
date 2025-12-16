package com.example.movilecibershield.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movilecibershield.data.local.TokenDataStore
import com.example.movilecibershield.data.repository.OrderRepository
import com.example.movilecibershield.data.repository.UserRepository

class UserViewModelFactory(
    private val repository: UserRepository,
    private val orderRepository: OrderRepository,
    private val tokenDataStore: TokenDataStore
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository, orderRepository, tokenDataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
