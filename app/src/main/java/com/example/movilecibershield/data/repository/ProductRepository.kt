package com.example.movilecibershield.data.repository

import com.example.movilecibershield.data.model.product.Product
import com.example.movilecibershield.data.model.product.ProductResponse
import com.example.movilecibershield.data.remote.api.product.ProductApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository(
    private val productApiService: ProductApiService
) {
    suspend fun getProducts(): RepoResult<List<Product>> = withContext(Dispatchers.IO) {
        try {
            val response = productApiService.getProduct()

            if (response.isSuccessful) {
                val dtos = response.body() ?: emptyList()

                val productos = dtos.map { dto ->
                    Product(
                        id = dto.id,
                        nombre = dto.productName,
                        precio = dto.price,
                        foto = dto.imageUrl ?: ""
                    )
                }

                RepoResult(data = productos)
            } else {
                RepoResult(error = "Error del servidor")
            }
        } catch (e: Exception) {
            RepoResult(error = e.message ?: "Error desconocido")
        }
    }
}

