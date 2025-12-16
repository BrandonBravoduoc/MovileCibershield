package com.example.movilecibershield.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movilecibershield.data.repository.OrderRepository
import com.example.movilecibershield.ui.viewmodel.CartViewModel

class CheckoutViewModelFactory(
    private val repository: OrderRepository,
    private val cartViewModel: CartViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CheckoutViewModel(repository, cartViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}