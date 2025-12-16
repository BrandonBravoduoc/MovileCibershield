package com.example.movilecibershield.data.remote.api.order

import com.example.movilecibershield.data.model.order.OrderCreate
import com.example.movilecibershield.data.model.order.OrderDetailResponse
import com.example.movilecibershield.data.model.order.OrderResponse
import com.example.movilecibershield.data.model.order.OrderStatusCreate
import com.example.movilecibershield.data.model.order.OrderStatusResponse
import com.example.movilecibershield.data.model.order.PaymentMethodResponse
import com.example.movilecibershield.data.model.order.PaymentResponse
import com.example.movilecibershield.data.model.order.ShippingMethodResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

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