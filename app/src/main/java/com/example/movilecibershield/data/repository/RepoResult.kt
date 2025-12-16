package com.example.movilecibershield.data.repository

data class RepoResult<T>(
    val data: T? = null,
    val error: String? = null
)