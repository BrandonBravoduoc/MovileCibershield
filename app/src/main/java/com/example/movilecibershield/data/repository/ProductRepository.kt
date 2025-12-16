package com.example.movilecibershield.data.repository

import com.example.movilecibershield.data.model.product.ProductResponse
import com.example.movilecibershield.data.remote.api.product.ProductApiService

class ProductRepository(private val productApiService: ProductApiService) {

    suspend fun getProducts(): RepoResult<List<ProductResponse>> {
        return try {
            val response = productApiService.getProduct()
            if (response.isSuccessful) {
                RepoResult(data = response.body())
            } else {
                RepoResult(error = response.errorBody()?.string())
            }
        } catch (e: Exception) {
            RepoResult(error = e.message)
        }
    }
}
