package com.example.movilecibershield.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movilecibershield.data.model.order.CreateItem
import com.example.movilecibershield.data.model.order.OrderCreate
import com.example.movilecibershield.data.model.order.PaymentMethodResponse
import com.example.movilecibershield.data.model.order.ShippingMethodResponse
import com.example.movilecibershield.data.repository.OrderRepository
import com.example.movilecibershield.ui.viewmodel.CartViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val repository: OrderRepository,
    private val cartViewModel: CartViewModel
) : ViewModel() {

    private val _paymentMethods = MutableStateFlow<List<PaymentMethodResponse>>(emptyList())
    val paymentMethods: StateFlow<List<PaymentMethodResponse>> = _paymentMethods

    private val _shippingMethods = MutableStateFlow<List<ShippingMethodResponse>>(emptyList())
    val shippingMethods: StateFlow<List<ShippingMethodResponse>> = _shippingMethods

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
                val paymentMethodsResult = repository.getAllPaymentMethods()
                if (paymentMethodsResult.data != null) {
                    _paymentMethods.value = paymentMethodsResult.data!!
                } else {
                    _error.value = paymentMethodsResult.error
                }

                val shippingMethodsResult = repository.getAllShippingMethods()
                if (shippingMethodsResult.data != null) {
                    _shippingMethods.value = shippingMethodsResult.data!!
                } else {
                    _error.value = shippingMethodsResult.error
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createOrder(
        paymentMethod: PaymentMethodResponse,
        shippingMethod: ShippingMethodResponse,
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

                val order = OrderCreate(
                    paymentMethodId = paymentMethod.id.toLong(),
                    shippingMethodId = shippingMethod.id.toLong(),
                    total = cartViewModel.getTotal().toString(),
                    cardInfo = cardInfo,
                    items = cartItems
                )

                val result = repository.createOrder(order)
                if (result.data != null) {
                    cartViewModel.confirmPurchase()
                    _purchaseStatus.value = "SUCCESS"
                } else {
                    _error.value = result.error
                    _purchaseStatus.value = "ERROR"
                }

            } catch (e: Exception) {
                _error.value = e.message
                _purchaseStatus.value = "ERROR"
            }
        }

    }

    fun clearStatus() {
        _purchaseStatus.value = null
    }
}
