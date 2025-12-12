package com.example.movilecibershield.data.repository

data class RepoResult<T>(
    val data: T? = null,
    val error: String? = null
) //--> Permite capturar todos los errores provenientes del backend. En el viewmodel ya no se crean excepciones personalizadas
// El backend es la única fuente de validaciones y errores de negocio.
