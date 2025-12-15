package com.example.movilecibershield.data.model.product

data class ProductResponse(
    val id: Long,
    val productName: String,
    val stock: Int,
    val price: Double,
    val imageUrl: String?,
    val subCategoryName: String?,
    val categoryName: String?,
    val tradeMarkName: String?
)

data class Product(
    val id: Long,
    val nombre: String,
    val precio: Double,
    val foto: String,
    val categoria: String?,
    val marca: String?
)
