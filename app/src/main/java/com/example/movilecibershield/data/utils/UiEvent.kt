package com.example.movilecibershield.data.utils

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
}