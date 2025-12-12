package com.example.movilecibershield.data.model.user

data class CommuneCreate(
    val nameCommunity: String,
    val regionId: Long
)

data class CommuneUpdate(
    val nameCommunity: String,
    val regionId: Long
)

data class CommuneResponse(
    val id: Long,
    val nameCommunity: String,
    val regionId: Long,
    val regionName: String
)

data class CommuneCombo(
    val id: Long,
    val nameCommunity: String,
    val regionName: String
)
