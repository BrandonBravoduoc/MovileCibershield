package com.example.movilecibershield.data.model.user

data class RoleCreate(
    val nameRole: String
)

data class RoleResponse(
    val id: Long,
    val nameRole: String
)
