package com.example.movilecibershield.data.repository

import com.example.movilecibershield.data.model.order.OrderCreate
import com.example.movilecibershield.data.model.order.OrderResponse
import com.example.movilecibershield.data.model.order.PaymentMethodResponse
import com.example.movilecibershield.data.model.order.ShippingMethodResponse
import com.example.movilecibershield.data.remote.api.order.OrderApiService
import com.example.movilecibershield.data.utils.extractErrorMessage
import retrofit2.HttpException
import java.io.IOException

class OrderRepository(
    private val orderApiService: OrderApiService
) {

    suspend fun getAllOrders(): RepoResult<List<OrderResponse>> {
        return try {
            val response = orderApiService.getAllOrders()
            RepoResult(data = response.body())
        } catch (e: HttpException) {
            RepoResult(error = extractErrorMessage(e))
        } catch (e: IOException) {
            RepoResult(error = "Sin conexión a internet")
        } catch (e: Exception) {
            RepoResult(error = "Error inesperado al cargar las órdenes")
        }
    }

    suspend fun createOrder(orderCreate: OrderCreate): RepoResult<OrderResponse> {
        return try {
            val response = orderApiService.createOrder(orderCreate)
            RepoResult(data = response.body())
        } catch (e: HttpException) {
            RepoResult(error = extractErrorMessage(e))
        } catch (e: IOException) {
            RepoResult(error = "Sin conexión a internet")
        } catch (e: Exception) {
            RepoResult(error = "Error inesperado al crear la orden")
        }
    }

    suspend fun getAllPaymentMethods(): RepoResult<List<PaymentMethodResponse>> {
        return try {
            val response = orderApiService.getAllPaymentMethods()
            RepoResult(data = response.body())
        } catch (e: HttpException) {
            RepoResult(error = extractErrorMessage(e))
        } catch (e: IOException) {
            RepoResult(error = "Sin conexión a internet")
        } catch (e: Exception) {
            RepoResult(error = "Error inesperado al cargar los métodos de pago")
        }
    }

    suspend fun getAllShippingMethods(): RepoResult<List<ShippingMethodResponse>> {
        return try {
            val response = orderApiService.getAllShippingMethods()
            RepoResult(data = response.body())
        } catch (e: HttpException) {
            RepoResult(error = extractErrorMessage(e))
        } catch (e: IOException) {
            RepoResult(error = "Sin conexión a internet")
        } catch (e: Exception) {
            RepoResult(error = "Error inesperado al cargar los métodos de envío")
        }
    }
}
