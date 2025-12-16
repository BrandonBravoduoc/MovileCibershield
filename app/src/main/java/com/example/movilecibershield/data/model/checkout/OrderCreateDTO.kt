package com.example.movilecibershield.data.model.checkout

import java.math.BigDecimal

data class OrderCreateDTO(
    val paymentMethodId: Long,
    val shippingMethodId: Long,
    val total: BigDecimal,
    val cardInfo: Map<String, String>,
    val items: List<CreateItem>
)

data class CreateItem(
    val productId: Long,
    val quantity: Int,
    val shippingMethodId: Int
)
