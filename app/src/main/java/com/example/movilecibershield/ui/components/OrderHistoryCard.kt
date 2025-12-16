package com.example.movilecibershield.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movilecibershield.data.model.order.OrderResponse

@Composable
fun OrderHistoryCard(
    orders: List<OrderResponse>
) {
    var selectedOrder by remember { mutableStateOf<OrderResponse?>(null) }

    if (selectedOrder != null) {
        OrderDetailDialog(
            order = selectedOrder!!,
            onDismiss = { selectedOrder = null }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Historial de compras", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
            if (orders.isEmpty()) {
                Text("No has realizado ninguna compra.")
            } else {
                orders.forEach { order ->
                    OrderRow(
                        order = order,
                        onShowDetails = { selectedOrder = it }
                    )
                }
            }
        }
    }
}
