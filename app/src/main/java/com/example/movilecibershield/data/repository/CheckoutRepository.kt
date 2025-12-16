package com.example.movilecibershield.data.repository

import com.example.movilecibershield.data.model.checkout.OrderCreateDTO
import com.example.movilecibershield.data.model.checkout.OrderResponseDTO
import com.example.movilecibershield.data.model.checkout.PaymentMethod
import com.example.movilecibershield.data.model.checkout.ShippingMethod
import com.example.movilecibershield.data.remote.CheckoutService

class CheckoutRepository(private val checkoutService: CheckoutService) {

    suspend fun getPaymentMethods(): List<PaymentMethod> {
        val response = checkoutService.getPaymentMethods()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception(response.errorBody()?.string())
        }
    }

    suspend fun getShippingMethods(): List<ShippingMethod> {
        val response = checkoutService.getShippingMethods()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception(response.errorBody()?.string())
        }
    }

    suspend fun createOrder(order: OrderCreateDTO): OrderResponseDTO {
        val response = checkoutService.createOrder(order)
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception(response.errorBody()?.string())
        }
    }
}