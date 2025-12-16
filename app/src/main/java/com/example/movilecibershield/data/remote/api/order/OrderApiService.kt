package com.example.movilecibershield.data.remote.api.order

import com.example.movilecibershield.data.model.order.OrderCreate
import com.example.movilecibershield.data.model.order.OrderResponse
import com.example.movilecibershield.data.model.order.PaymentMethodResponse
import com.example.movilecibershield.data.model.order.ShippingMethodResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OrderApiService {
    @GET("api/v1/orders")
    suspend fun getAllOrders(): Response<List<OrderResponse>>

    @POST("api/v1/orders")
    suspend fun createOrder(
        @Body orderCreate: OrderCreate
    ): Response<OrderResponse>

    @GET("api/v1/payment-methods")
    suspend fun getAllPaymentMethods(): Response<List<PaymentMethodResponse>>

    @GET("api/v1/shipping-methods")
    suspend fun getAllShippingMethods(): Response<List<ShippingMethodResponse>>
}