package com.example.movilecibershield.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movilecibershield.data.model.product.ProductResponse
import com.example.movilecibershield.data.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    var products by mutableStateOf<List<ProductResponse>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val result = productRepository.getProducts()

            isLoading = false
            result.data?.let {
                products = it
            }
            result.error?.let {
                errorMessage = it
            }
        }
    }
}