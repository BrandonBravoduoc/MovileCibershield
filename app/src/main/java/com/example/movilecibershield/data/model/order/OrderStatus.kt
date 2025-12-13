package com.example.movilecibershield.data.model.order

data class OrderStatusResponse(
    val id: Long,
    val name: String,
    val description: String
)

data class OrderStatusCombo(
    val id: Long,
    val name: String
)

data class OrderStatusCreate(
    val id: Long,
    val name: String
)