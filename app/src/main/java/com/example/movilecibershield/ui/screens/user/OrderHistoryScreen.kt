package com.example.movilecibershield.ui.screens.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.movilecibershield.data.model.order.OrderResponse
import com.example.movilecibershield.ui.components.OrderDetailDialog
import com.example.movilecibershield.ui.components.OrderRow
import com.example.movilecibershield.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    navController: NavController,
    viewModel: UserViewModel
) {
    val orders by viewModel.orders.collectAsState()
    val loading by viewModel.loading.collectAsState()
    var selectedOrder by remember { mutableStateOf<OrderResponse?>(null) }

    // ✅ CORRECCIÓN: Se elimina la llamada a `loadOrders()`.
    // La carga ahora se inicia desde la pantalla de perfil para evitar condiciones de carrera.

    selectedOrder?.let {
        OrderDetailDialog(
            order = it,
            onDismiss = { selectedOrder = null }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de Compras") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (loading && orders.isEmpty()) { // Muestra el indicador solo si está cargando y no hay órdenes antiguas que mostrar.
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (orders.isEmpty()) {
                Text("No tienes pedidos aún.", modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(orders) {
                        OrderRow(
                            order = it,
                            onShowDetails = { order -> selectedOrder = order }
                        )
                    }
                }
            }
        }
    }
}
