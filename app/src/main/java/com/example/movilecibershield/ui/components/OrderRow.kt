package com.example.movilecibershield.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.movilecibershield.data.model.order.OrderResponse

@Composable
fun OrderRow(
    order: OrderResponse,
    onShowDetails: (OrderResponse) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Orden #${order.orderNumber}", fontWeight = FontWeight.Bold)
            Text("Fecha: ${order.orderDate}")
            Text("Total: $${order.total}")
            Text("Estado: ${order.status.name}")
            Button(
                onClick = { onShowDetails(order) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver detalles")
            }
        }
    }
}
