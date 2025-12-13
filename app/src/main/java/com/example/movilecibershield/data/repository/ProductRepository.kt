package com.example.movilecibershield.data.repository

import com.example.movilecibershield.data.model.product.ProductResponse
import com.example.movilecibershield.data.remote.api.product.ProductService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException

class ProductRepository(
    private val productService: ProductService
) {
    suspend fun getProducts(): RepoResult<List<ProductResponse>> = withContext(Dispatchers.IO) {
        try {
            // Llamamos al servicio (que definimos anteriormente)
            val response = productService.getProduct()

            if (response.isSuccessful) {
                // Si sale bien
                val products = response.body() ?: emptyList<ProductResponse>()
                RepoResult(data = products)
            } else {
                // Si el servidor responde error
                val errorBody = response.errorBody()?.string()
                val message = if (!errorBody.isNullOrBlank()) {
                    try {
                        JSONObject(errorBody).optString("message", "Error al cargar productos")
                    } catch (_: Exception) {
                        "Error del servidor"
                    }
                } else {
                    "Error desconocido"
                }
                RepoResult(error = message)
            }
        } catch (e: IOException) {
            RepoResult(error = "Sin conexión a internet. Verifique su red. Error: ${e.message}")
        } catch (e: Exception) {
            RepoResult(error = "Error inesperado: ${e.message}")
        }
    }
}
