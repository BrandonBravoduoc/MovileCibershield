package com.example.movilecibershield.data.model.user

data class AddressCreate(
    val street: String,
    val number: String,
    val communeId: Long
)

data class AddressUpdate(
    val street: String,
    val number: String,
    val communeId: Long
)

data class AddressResponse(
    val id: Long,
    val street: String,
    val number: String,
    val communeId: Long,
    val communeName: String,
    val regionId: Long,
    val regionName: String
)

data class AddressCombo(
    val id: Long,
    val fullAddress: String
)
