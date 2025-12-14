package com.example.movilecibershield.data.model.order

data class OrderCreate(
    val paymentMethodId: Long,
    val shippingMethodId: Long,
    val total: String,
    val cardInfo: Map<String, String>,
    val items: List<CreateItem>
)

data class CreateItem(
    val productId: Long,
    val quantity: Int,
    val shippingMethodId: Int
)

data class OrderDetailResponse(
    val productId: Long,
    val productName: String,
    val amount: Int,
    val unitPrice: String,
    val subtotal: String
)

data class OrderResponse(
    val id: Long,
    val orderNumber: String,
    val orderDate: String,
    val total: String,
    val status: String,
    val details: List<OrderDetailResponse>
)