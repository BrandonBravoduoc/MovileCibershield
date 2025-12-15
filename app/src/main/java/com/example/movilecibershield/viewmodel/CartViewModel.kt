package com.example.movilecibershield.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movilecibershield.data.model.product.Product
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CartItem(
    val product: Product,
    val quantity: Int
) {
    val totalPrice: Double
        get() = product.precio * quantity
}

class CartViewModel : ViewModel() {
    private val _cartMap = MutableStateFlow<Map<Long, CartItem>>(emptyMap())

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems = _cartItems.asStateFlow()

    private val _purchaseStatus = MutableStateFlow<String?>(null)
    val purchaseStatus = _purchaseStatus.asStateFlow()

    init {
        viewModelScope.launch {
            _cartMap.collect { map ->
                _cartItems.value = map.values.toList().sortedBy { it.product.nombre }
            }
        }
    }

    fun addToCart(product: Product) {
        val currentMap = _cartMap.value.toMutableMap()
        val existingItem = currentMap[product.id]

        if (existingItem != null) {
            currentMap[product.id] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
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

    fun getTotalItemsCount(): Int {
        return _cartItems.value.sumOf { it.quantity }
    }

    fun confirmPurchase() {
        viewModelScope.launch {
            _purchaseStatus.value = "LOADING"
            delay(2000)

            _cartMap.value = emptyMap()
            _purchaseStatus.value = "SUCCESS"

            delay(3000)
            _purchaseStatus.value = null
        }
    }

    fun clearStatus() {
        _purchaseStatus.value = null
    }
}
