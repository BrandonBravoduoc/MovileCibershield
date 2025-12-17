package com.example.movilecibershield.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movilecibershield.data.model.order.CreateItem
import com.example.movilecibershield.data.model.order.OrderCreate
import com.example.movilecibershield.data.model.order.PaymentMethodResponse
import com.example.movilecibershield.data.model.order.ShippingMethodResponse
import com.example.movilecibershield.data.repository.OrderRepository
import com.example.movilecibershield.data.utils.UiEvent
import com.example.movilecibershield.ui.viewmodel.CartViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val repository: OrderRepository,
    private val cartViewModel: CartViewModel
) : ViewModel() {

    private val _paymentMethods = MutableStateFlow<List<PaymentMethodResponse>>(emptyList())
    val paymentMethods: StateFlow<List<PaymentMethodResponse>> = _paymentMethods.asStateFlow()

    private val _shippingMethods = MutableStateFlow<List<ShippingMethodResponse>>(emptyList())
    val shippingMethods: StateFlow<List<ShippingMethodResponse>> = _shippingMethods.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _purchaseStatus = MutableStateFlow<String?>(null)
    val purchaseStatus: StateFlow<String?> = _purchaseStatus.asStateFlow()

    /* ========================
       UI EVENTS (Snackbar)
       ======================== */
    private val _uiEvent = Channel<UiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        fetchInitialData()
    }

    private fun fetchInitialData() {
        viewModelScope.launch {
            _isLoading.value = true

            val paymentResult = repository.getAllPaymentMethods()
            paymentResult.data?.let {
                _paymentMethods.value = it
            } ?: run {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(
                        paymentResult.error ?: "Error al cargar métodos de pago"
                    )
                )
            }

            val shippingResult = repository.getAllShippingMethods()
            shippingResult.data?.let {
                _shippingMethods.value = it
            } ?: run {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(
                        shippingResult.error ?: "Error al cargar métodos de envío"
                    )
                )
            }

            _isLoading.value = false
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
                val items = cartViewModel.cartItems.value.map {
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
                    items = items
                )

                val result = repository.createOrder(order)

                if (result.data != null) {
                    cartViewModel.confirmPurchase()
                    _purchaseStatus.value = "SUCCESS"
                } else {
                    _purchaseStatus.value = "ERROR"
                    _uiEvent.send(
                        UiEvent.ShowSnackbar(
                            result.error ?: "Error al procesar la compra"
                        )
                    )
                }

            } catch (e: Exception) {
                _purchaseStatus.value = "ERROR"
                _uiEvent.send(
                    UiEvent.ShowSnackbar(
                        e.message ?: "Error inesperado al crear la orden"
                    )
                )
            }
        }
    }

    fun clearStatus() {
        _purchaseStatus.value = null
    }
}
