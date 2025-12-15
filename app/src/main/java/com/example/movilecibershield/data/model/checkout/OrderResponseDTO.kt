package com.example.movilecibershield.data.model.checkout

import java.math.BigDecimal

data class OrderResponseDTO(
    val id: Long,
    val orderNumber: String,
    val orderDate: String, // Changed to String to avoid LocalDate issues with Android versions
    val total: BigDecimal,
    val status: String,
    val details: List<OrderDetailResponse>
)

data class OrderDetailResponse(
    val productId: Long,
    val productName: String,
    val amount: Int,
    val unitPrice: BigDecimal,
    val subtotal: BigDecimal
)
