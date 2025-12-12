package com.example.movilecibershield.data.model.user

data class RegionCreate(
    val regionName: String
)

data class RegionUpdate(
    val regionName: String
)

data class RegionResponse(
    val id: Long,
    val regionName: String,
    val communesCount: Int
)

data class RegionCombo(
    val id: Long,
    val regionName: String
)