package com.example.movilecibershield.data.remote

import com.example.movilecibershield.data.model.checkout.OrderCreateDTO
import com.example.movilecibershield.data.model.checkout.OrderResponseDTO
import com.example.movilecibershield.data.model.checkout.PaymentMethod
import com.example.movilecibershield.data.model.checkout.ShippingMethod
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CheckoutService {
    @GET("api/v1/payment-methods")
    suspend fun getPaymentMethods(): Response<List<PaymentMethod>>

    @GET("api/v1/shipping-methods")
    suspend fun getShippingMethods(): Response<List<ShippingMethod>>

    @POST("api/v1/orders")
    suspend fun createOrder(@Body order: OrderCreateDTO): Response<OrderResponseDTO>
}
