package com.example.movilecibershield.data.model.user

data class ContactCreateWithAddress(
    val name: String,
    val lastName: String,
    val phone: String,
    val street: String,
    val number: String,
    val communeId: Long
)

data class ContactUpdateWithAddress(
    val id: Long,
    val name: String,
    val lastName: String,
    val phone: String,
    val street: String,
    val number: String,
    val communeId: Long
)

data class ContactResponse(
    val id: Long,
    val name: String,
    val lastName: String,
    val phone: String,
    val addressInfo: String,
    val userName: String
)


