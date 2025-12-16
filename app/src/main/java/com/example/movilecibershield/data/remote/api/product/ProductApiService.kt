package com.example.movilecibershield.data.remote.api.product

import com.example.movilecibershield.data.model.product.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApiService {

    @GET("api/v1/products")
    suspend fun getProduct(): Response<List<ProductResponse>>

    @GET("api/v1/products/{id}")
    suspend fun getProductById(@Path("id") id: Long): Response<ProductResponse>
}