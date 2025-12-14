package com.example.movilecibershield.data.model.order

data class ShippingMethodResponse(
    val id: Int,
    val methodName: String,
    val shippingCost: String,
    val activeStatus: Boolean
)

data class ShippingMethodCombo(
    val id: Long,
    val methodName: String,
    val shippingCost: String
)