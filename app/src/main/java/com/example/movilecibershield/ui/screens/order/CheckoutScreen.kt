package com.example.movilecibershield.ui.screens.order

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.movilecibershield.data.model.order.PaymentMethodResponse
import com.example.movilecibershield.data.model.order.ShippingMethodResponse
import com.example.movilecibershield.navigation.Routes
import com.example.movilecibershield.viewmodel.CheckoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    viewModel: CheckoutViewModel = viewModel()
) {
    val context = LocalContext.current
    val paymentMethods by viewModel.paymentMethods.collectAsState()
    val shippingMethods by viewModel.shippingMethods.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val purchaseStatus by viewModel.purchaseStatus.collectAsState()

    var selectedPaymentMethod by remember { mutableStateOf<PaymentMethodResponse?>(null) }
    var selectedShippingMethod by remember { mutableStateOf<ShippingMethodResponse?>(null) }
    var cardHolderName by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    var paymentMenuExpanded by remember { mutableStateOf(false) }
    var shippingMenuExpanded by remember { mutableStateOf(false) }

    val backgroundBrush = remember {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF111827),
                Color(0xFF020617)
            )
        )
    }
    val topBarColor = Color(0xFF111827)

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = Color.Gray,
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = Color.Gray,
        cursorColor = Color.White,
        focusedTrailingIconColor = Color.White,
        unfocusedTrailingIconColor = Color.Gray
    )

    LaunchedEffect(purchaseStatus) {
        if (purchaseStatus == "SUCCESS") {
            Toast.makeText(context, "Gracias por su compra", Toast.LENGTH_LONG).show()
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.CHECKOUT) { inclusive = true }
            }
            viewModel.clearStatus()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Realizar compra", color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = topBarColor
                )
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
                .padding(padding)
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else if (error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: $error", color = MaterialTheme.colorScheme.error)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // --- DROPDOWN PAGO ---
                    ExposedDropdownMenuBox(
                        expanded = paymentMenuExpanded,
                        onExpandedChange = { paymentMenuExpanded = !paymentMenuExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedPaymentMethod?.paymentName ?: "",
                            onValueChange = { },
                            label = { Text("Método de pago") },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = paymentMenuExpanded) },
                            colors = textFieldColors
                        )
                        ExposedDropdownMenu(
                            expanded = paymentMenuExpanded,
                            onDismissRequest = { paymentMenuExpanded = false }
                        ) {
                            paymentMethods.forEach {
                                DropdownMenuItem(
                                    text = { Text(it.paymentName) },
                                    onClick = {
                                        selectedPaymentMethod = it
                                        paymentMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    ExposedDropdownMenuBox(
                        expanded = shippingMenuExpanded,
                        onExpandedChange = { shippingMenuExpanded = !shippingMenuExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedShippingMethod?.methodName ?: "",
                            onValueChange = { },
                            label = { Text("Método de envío") },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = shippingMenuExpanded) },
                            colors = textFieldColors // Aplicar colores oscuros
                        )
                        ExposedDropdownMenu(
                            expanded = shippingMenuExpanded,
                            onDismissRequest = { shippingMenuExpanded = false }
                        ) {
                            shippingMethods.forEach {
                                DropdownMenuItem(
                                    text = { Text(it.methodName) },
                                    onClick = {
                                        selectedShippingMethod = it
                                        shippingMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Información de la tarjeta",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = cardHolderName,
                        onValueChange = { cardHolderName = it },
                        label = { Text("Nombre del titular") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = cardNumber,
                        onValueChange = { cardNumber = it },
                        label = { Text("Número de tarjeta") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = expiryDate,
                            onValueChange = { expiryDate = it },
                            label = { Text("Fecha de expiración") },
                            modifier = Modifier.weight(1f),
                            colors = textFieldColors
                        )
                        OutlinedTextField(
                            value = cvv,
                            onValueChange = { cvv = it },
                            label = { Text("CVV") },
                            modifier = Modifier.weight(1f),
                            colors = textFieldColors // Aplicar colores oscuros
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.White
                            ),
                            border = BorderStroke(1.dp, Color.Gray)
                        ) {
                            Text("Cancelar")
                        }

                        Button(
                            onClick = {
                                val payment = selectedPaymentMethod
                                val shipping = selectedShippingMethod

                                when {
                                    payment == null -> {
                                        Toast.makeText(context, "Por favor, selecciona un método de pago", Toast.LENGTH_SHORT).show()
                                    }
                                    shipping == null -> {
                                        Toast.makeText(context, "Por favor, selecciona un método de envío", Toast.LENGTH_SHORT).show()
                                    }
                                    cardHolderName.isBlank() -> {
                                        Toast.makeText(context, "Por favor, ingresa el nombre del titular", Toast.LENGTH_SHORT).show()
                                    }
                                    cardNumber.isBlank() -> {
                                        Toast.makeText(context, "Por favor, ingresa el número de tarjeta", Toast.LENGTH_SHORT).show()
                                    }
                                    expiryDate.isBlank() -> {
                                        Toast.makeText(context, "Por favor, ingresa la fecha de expiración", Toast.LENGTH_SHORT).show()
                                    }
                                    cvv.isBlank() -> {
                                        Toast.makeText(context, "Por favor, ingresa el CVV", Toast.LENGTH_SHORT).show()
                                    }
                                    else -> {
                                        viewModel.createOrder(
                                            paymentMethod = payment,
                                            shippingMethod = shipping,
                                            cardHolderName = cardHolderName,
                                            cardNumber = cardNumber,
                                            expiryDate = expiryDate,
                                            cvv = cvv
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            enabled = purchaseStatus != "LOADING",
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                disabledContainerColor = Color.Gray
                            )
                        ) {
                            if (purchaseStatus == "LOADING") {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                            } else {
                                Text("Pagar Ahora", color = Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
