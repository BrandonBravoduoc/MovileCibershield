package com.example.movilecibershield.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movilecibershield.data.model.product.Product
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    // Lista de productos en el carrito
    private val _cartItems = MutableStateFlow<List<Product>>(emptyList())
    val cartItems = _cartItems.asStateFlow()

    // Estado de la compra (para mostrar carga o éxito)
    private val _purchaseStatus = MutableStateFlow<String?>(null) // null = nada, "LOADING", "SUCCESS", "ERROR"
    val purchaseStatus = _purchaseStatus.asStateFlow()

    // Agregar producto (lo llamas desde el Home)
    fun addToCart(product: Product) {
        // Truco: Creamos una lista nueva agregando el producto
        _cartItems.value = _cartItems.value + product
    }

    // Calcular total
    fun getTotal(): Double {
        return _cartItems.value.sumOf { it.precio }
    }

    // Función "Pagar" (Simula conexión a API)
    fun confirmPurchase() {
        viewModelScope.launch {
            _purchaseStatus.value = "LOADING"

            // AQUÍ SIMULAMOS LA LLAMADA A LA API (Retrofit)
            delay(2000) // Esperamos 2 segundos para que parezca real

            // Vaciamos el carrito
            _cartItems.value = emptyList()
            _purchaseStatus.value = "SUCCESS"

            // Limpiamos el estado de éxito después de un ratito
            delay(3000)
            _purchaseStatus.value = null
        }
    }

    fun clearStatus() {
        _purchaseStatus.value = null
    }
}
