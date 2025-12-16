package com.example.movilecibershield.data.repository

import com.example.movilecibershield.data.model.order.OrderCreate
import com.example.movilecibershield.data.remote.api.order.OrderApiService

class OrderRepository(private val orderApiService: OrderApiService) {

    suspend fun getAllOrders() = try {
        val response = orderApiService.getAllOrders()
        if (response.isSuccessful) {
            RepoResult(data = response.body())
        } else {
            RepoResult(error = response.message())
        }
    } catch (e: Exception) {
        RepoResult(error = e.message ?: "Error desconocido")
    }

    suspend fun createOrder(orderCreate: OrderCreate) = try {
        val response = orderApiService.createOrder(orderCreate)
        if (response.isSuccessful) {
            RepoResult(data = response.body())
        } else {
            RepoResult(error = response.message())
        }
    } catch (e: Exception) {
        RepoResult(error = e.message ?: "Error desconocido")
    }

    suspend fun getAllPaymentMethods() = try {
        val response = orderApiService.getAllPaymentMethods()
        if (response.isSuccessful) {
            RepoResult(data = response.body())
        } else {
            RepoResult(error = response.message())
        }
    } catch (e: Exception) {
        RepoResult(error = e.message ?: "Error desconocido")
    }


    suspend fun getAllShippingMethods() = try {
        val response = orderApiService.getAllShippingMethods()
        if (response.isSuccessful) {
            RepoResult(data = response.body())
        } else {
            RepoResult(error = response.message())
        }
    } catch (e: Exception) {
        RepoResult(error = e.message ?: "Error desconocido")
    }

}
