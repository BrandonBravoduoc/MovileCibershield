package com.example.movilecibershield.data.remote.api.product

import com.example.movilecibershield.data.model.product.ProductResponse
import retrofit2.Response
import retrofit2.http.GET

interface ProductApiService {

    @GET("api/v1/products")
    suspend fun getProduct(): Response<List<ProductResponse>>


}