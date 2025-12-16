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

data class ProductInOrder(
    val id: Long,
    val productName: String
)

data class OrderDetailResponse(

    val product: ProductInOrder,
    val quantity: Int,
    val unitPrice: String,
    val subtotal: String
)

data class OrderResponse(
    val id: Long,
    val orderNumber: String,
    val orderDate: String,
    val total: String,
    val status: OrderStatusResponse,
    val details: List<OrderDetailResponse>
)