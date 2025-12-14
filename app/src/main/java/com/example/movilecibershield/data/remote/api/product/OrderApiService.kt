package com.example.movilecibershield.data.remote.api

import com.example.movilecibershield.data.model.order.*
import retrofit2.Response
import retrofit2.http.*

interface OrderApiService {

    @GET("api/v1/orders")
    suspend fun getAllOrders(): Response<List<OrderResponse>>

    @GET("api/v1/orders/{id}")
    suspend fun getOrderById(
        @Path("id") id: Long
    ): Response<OrderResponse>

    @POST("api/v1/orders")
    suspend fun createOrder(
        @Body orderCreate: OrderCreate
    ): Response<OrderResponse>

    @PATCH("api/v1/orders/{id}/status")
    suspend fun updateOrderStatus(
        @Path("id") id: Long,
        @Body status: OrderStatusCreate
    ): Response<OrderResponse>

    @GET("api/v1/order-details")
    suspend fun getAllOrderDetails(): Response<List<OrderDetailResponse>>

    @GET("api/v1/order-details/{id}")
    suspend fun getOrderDetailById(
        @Path("id") id: Long
    ): Response<OrderDetailResponse>

    @GET("api/v1/order-status")
    suspend fun getAllOrderStatus(): Response<List<OrderStatusResponse>>

    @GET("api/v1/order-status/{id}")
    suspend fun getOrderStatusById(
        @Path("id") id: Long
    ): Response<OrderStatusResponse>

    @PATCH("api/v1/order-status/{id}")
    suspend fun updateOrderStatusCatalog(
        @Path("id") id: Long,
        @Body status: OrderStatusCreate
    ): Response<OrderStatusResponse>

    @GET("api/v1/payments")
    suspend fun getAllPayments(): Response<List<PaymentResponse>>

    @GET("api/v1/payments/{id}")
    suspend fun getPaymentById(
        @Path("id") id: Long
    ): Response<PaymentResponse>

    @GET("api/v1/payment-methods")
    suspend fun getAllPaymentMethods(): Response<List<PaymentMethodResponse>>

    @GET("api/v1/payment-methods/{id}")
    suspend fun getPaymentMethodById(
        @Path("id") id: Long
    ): Response<PaymentMethodResponse>

    @GET("api/v1/shipping-methods")
    suspend fun getAllShippingMethods(): Response<List<ShippingMethodResponse>>

    @GET("api/v1/shipping-methods/{id}")
    suspend fun getShippingMethodById(
        @Path("id") id: Int
    ): Response<ShippingMethodResponse>
}
