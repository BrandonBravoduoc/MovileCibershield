package com.example.movilecibershield.data.utils

import com.google.gson.JsonParser
import retrofit2.HttpException

fun extractErrorMessage(e: HttpException): String {
    return try {
        val errorBody = e.response()?.errorBody()?.string()

        if (errorBody.isNullOrBlank()) {
            "Error del servidor (${e.code()})"
        } else {
            try {
                val json = JsonParser.parseString(errorBody).asJsonObject
                json["message"]?.asString ?: errorBody
            } catch (_: Exception) {
                errorBody
            }
        }
    } catch (_: Exception) {
        "Error del servidor"
    }
}
