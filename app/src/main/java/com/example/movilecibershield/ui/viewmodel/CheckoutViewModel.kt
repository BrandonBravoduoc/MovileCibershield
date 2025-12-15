package com.example.movilecibershield.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movilecibershield.data.model.checkout.CreateItem
import com.example.movilecibershield.data.model.checkout.OrderCreateDTO
import com.example.movilecibershield.data.model.checkout.PaymentMethod
import com.example.movilecibershield.data.model.checkout.ShippingMethod
import com.example.movilecibershield.data.repository.CheckoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal

class CheckoutViewModel(
    private val repository: CheckoutRepository,
    private val cartViewModel: CartViewModel
) : ViewModel() {

    private val _paymentMethods = MutableStateFlow<List<PaymentMethod>>(emptyList())
    val paymentMethods: StateFlow<List<PaymentMethod>> = _paymentMethods

    private val _shippingMethods = MutableStateFlow<List<ShippingMethod>>(emptyList())
    val shippingMethods: StateFlow<List<ShippingMethod>> = _shippingMethods

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _purchaseStatus = MutableStateFlow<String?>(null)
    val purchaseStatus: StateFlow<String?> = _purchaseStatus

    init {
        fetchInitialData()
    }

    private fun fetchInitialData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _paymentMethods.value = repository.getPaymentMethods()
                _shippingMethods.value = repository.getShippingMethods()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createOrder(
        paymentMethod: PaymentMethod,
        shippingMethod: ShippingMethod,
        cardHolderName: String,
        cardNumber: String,
        expiryDate: String,
        cvv: String
    ) {
        viewModelScope.launch {
            _purchaseStatus.value = "LOADING"
            try {
                val cartItems = cartViewModel.cartItems.value.map {
                    CreateItem(
                        productId = it.product.id,
                        quantity = it.quantity,
                        shippingMethodId = shippingMethod.id
                    )
                }

                val cardInfo = mapOf(
                    "cardHolderName" to cardHolderName,
                    "cardNumber" to cardNumber,
                    "expiryDate" to expiryDate,
                    "cvv" to cvv
                )

                val order = OrderCreateDTO(
                    paymentMethodId = paymentMethod.id,
                    shippingMethodId = shippingMethod.id.toLong(),
                    total = BigDecimal(cartViewModel.getTotal()),
                    cardInfo = cardInfo,
                    items = cartItems
                )

                repository.createOrder(order)
                cartViewModel.confirmPurchase()
                _purchaseStatus.value = "SUCCESS"
            } catch (e: Exception) {
                _error.value = e.message
                _purchaseStatus.value = "ERROR"
            }
        }
    }
}