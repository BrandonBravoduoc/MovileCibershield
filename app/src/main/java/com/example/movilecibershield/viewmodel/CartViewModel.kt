package com.example.movilecibershield.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movilecibershield.data.model.product.Product
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Clase auxiliar para representar un item en el carrito agrupado
data class CartItem(
    val product: Product,
    val quantity: Int
) {
    val totalPrice: Double
        get() = product.precio * quantity
}

class CartViewModel : ViewModel() {

    // Mapa interno para manejar productos y cantidades
    // Key: ID del producto, Value: CartItem
    private val _cartMap = MutableStateFlow<Map<Long, CartItem>>(emptyMap())
    
    // Lista expuesta para la UI (derivada del mapa)
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems = _cartItems.asStateFlow()

    // Estado de la compra (para mostrar carga o éxito)
    private val _purchaseStatus = MutableStateFlow<String?>(null) // null = nada, "LOADING", "SUCCESS", "ERROR"
    val purchaseStatus = _purchaseStatus.asStateFlow()

    init {
        // Observar cambios en el mapa para actualizar la lista
        viewModelScope.launch {
            _cartMap.collect { map ->
                _cartItems.value = map.values.toList().sortedBy { it.product.nombre }
            }
        }
    }

    // Agregar producto (ahora suma cantidad si ya existe)
    fun addToCart(product: Product) {
        val currentMap = _cartMap.value.toMutableMap()
        val existingItem = currentMap[product.id]

        if (existingItem != null) {
            // Si ya existe, incrementamos la cantidad
            currentMap[product.id] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            // Si no existe, lo agregamos con cantidad 1
            currentMap[product.id] = CartItem(product, 1)
        }
        
        _cartMap.value = currentMap
    }
    
    fun removeFromCart(productId: Long) {
        val currentMap = _cartMap.value.toMutableMap()
        val existingItem = currentMap[productId]

        if (existingItem != null) {
            if (existingItem.quantity > 1) {
                currentMap[productId] = existingItem.copy(quantity = existingItem.quantity - 1)
            } else {
                currentMap.remove(productId)
            }
            _cartMap.value = currentMap
        }
    }

    fun deleteProduct(productId: Long) {
        val currentMap = _cartMap.value.toMutableMap()
        currentMap.remove(productId)
        _cartMap.value = currentMap
    }

    // Calcular total
    fun getTotal(): Double {
        return _cartItems.value.sumOf { it.totalPrice }
    }
    
    // Total de items individuales (para badges, etc.)
    fun getTotalItemsCount(): Int {
        return _cartItems.value.sumOf { it.quantity }
    }

    // Función "Pagar" (Simula conexión a API)
    fun confirmPurchase() {
        viewModelScope.launch {
            _purchaseStatus.value = "LOADING"

            // AQUÍ SIMULAMOS LA LLAMADA A LA API (Retrofit)
            delay(2000) // Esperamos 2 segundos para que parezca real

            // Vaciamos el carrito
            _cartMap.value = emptyMap()
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
