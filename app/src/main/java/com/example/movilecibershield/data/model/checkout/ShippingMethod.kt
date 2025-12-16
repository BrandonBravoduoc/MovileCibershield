package com.example.movilecibershield.data.model.checkout

import java.math.BigDecimal

data class ShippingMethod(
    val id: Int,
    val methodName: String,
    val shippingCost: BigDecimal,
    val activeStatus: Boolean
)
