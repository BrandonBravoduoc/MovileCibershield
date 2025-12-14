package com.example.movilecibershield.data.model.order

data class PaymentCreate(
    val amount: String,
    val paymentDate: String,
    val paymentMethodId: Long,
    val orderId: Long
)

data class PaymentResponse(
    val id: Long,
    val amount: String,
    val paymentDate: String,
    val paymentMethodId: Long,
    val paymentMethodName: String,
    val orderId: Long
)