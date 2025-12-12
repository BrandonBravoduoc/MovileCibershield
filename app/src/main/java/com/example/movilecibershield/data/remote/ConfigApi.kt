package com.example.movilecibershield.data.remote

import com.example.movilecibershield.data.remote.api.AuthApiService
import com.example.movilecibershield.data.remote.api.product.ProductService
import com.example.movilecibershield.data.remote.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ConfigApi {
    private const val BASE_URL = "https://cibershield-backend.onrender.com/"
    private val okHttpClient : OkHttpClient by lazy{
        OkHttpClient.Builder()

            .addInterceptor(AuthInterceptor()) //--> Agrega el token del inicio de sesión
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    val  productService: ProductService by lazy {
        retrofit.create(ProductService::class.java)
    }

}