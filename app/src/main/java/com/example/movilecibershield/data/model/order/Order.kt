package com.example.movilecibershield.data.model.order

import com.example.movilecibershield.data.model.order.OrderStatusResponse

// --- Clases para la creación de órdenes ---
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

// --- Clases para la respuesta de la API ---

// NUEVO: Representa el objeto "user" anidado que envía la API.
data class UserInOrder(
    val id: Long
)

// NUEVO: Representa el objeto "product" anidado en los detalles.
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
    // ✅ CORRECCIÓN: Se añade el campo 'user' para poder filtrar.
    val user: UserInOrder,
    val details: List<OrderDetailResponse>
)
