package com.example.movilecibershield.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.movilecibershield.ui.viewmodel.CartViewModel
import android.widget.Toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: CartViewModel,
    navController: NavController
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val status by viewModel.purchaseStatus.collectAsState()
    val context = LocalContext.current

    // Efecto para mostrar mensaje de éxito
    LaunchedEffect(status) {
        if (status == "SUCCESS") {
            Toast.makeText(context, "¡Compra realizada con éxito!", Toast.LENGTH_LONG).show()
            // Opcional: Volver al home automáticamente
            // navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Mi Carrito") })
        },
        bottomBar = {
            // BARRA DE PAGO
            if (cartItems.isNotEmpty()) {
                Button(
                    onClick = { viewModel.confirmPurchase() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    enabled = status != "LOADING"
                ) {
                    if (status == "LOADING") {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Pagar $${viewModel.getTotal()}")
                    }
                }
            }
        }
    ) { padding ->
        if (cartItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Tu carrito está vacío")
            }
        } else {
            LazyColumn(
                contentPadding = padding,
                modifier = Modifier.fillMaxSize()
            ) {
                items(cartItems) { product ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp)) {
                            // Info del producto
                            Column {
                                Text(text = product.nombre, style = MaterialTheme.typography.titleMedium)
                                Text(text = "$ ${product.precio}", style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }
            }
        }
    }
}
