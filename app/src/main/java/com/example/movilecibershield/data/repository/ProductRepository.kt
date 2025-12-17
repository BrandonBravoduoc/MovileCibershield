package com.example.movilecibershield.data.repository

import com.example.movilecibershield.data.model.product.ProductResponse
import com.example.movilecibershield.data.remote.api.product.ProductApiService
import com.example.movilecibershield.data.utils.extractErrorMessage
import retrofit2.HttpException
import java.io.IOException

class ProductRepository(
    private val productApiService: ProductApiService
) {

    suspend fun getProducts(): RepoResult<List<ProductResponse>> {
        return try {
            val response = productApiService.getProduct()
            RepoResult(data = response.body())
        } catch (e: HttpException) {
            RepoResult(error = extractErrorMessage(e))
        } catch (e: IOException) {
            RepoResult(error = "Sin conexión a internet")
        } catch (e: Exception) {
            RepoResult(error = "Error inesperado al cargar los productos")
        }
    }
}
