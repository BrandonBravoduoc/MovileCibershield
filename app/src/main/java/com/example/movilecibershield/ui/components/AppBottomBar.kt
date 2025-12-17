package com.example.movilecibershield.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.movilecibershield.data.local.TokenDataStore
import com.example.movilecibershield.navigation.Routes

@Composable
fun AppBottomBar(
    navController: NavController,
    currentRoute: String?,
    tokenDataStore: TokenDataStore,
    excludedRoute: String? = null
) {
    val token by tokenDataStore.getToken().collectAsState(initial = null)

    NavigationBar {


        if (excludedRoute != Routes.HOME) {
            NavigationBarItem(
                selected = currentRoute == Routes.HOME,
                onClick = { navController.navigate(Routes.HOME) },
                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                label = { Text("Inicio") }
            )
        }


        if (excludedRoute != Routes.CART) {
            NavigationBarItem(
                selected = currentRoute == Routes.CART,
                onClick = { navController.navigate(Routes.CART) },
                icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
                label = { Text("Carrito") }
            )
        }

        if (excludedRoute == Routes.PROFILE) {
            NavigationBarItem(
                selected = currentRoute == Routes.ORDER_HISTORY,
                onClick = { navController.navigate(Routes.ORDER_HISTORY) },
                icon = { Icon(Icons.Default.History, contentDescription = null) },
                label = { Text("Historial") }
            )
        }


        if (excludedRoute != Routes.PROFILE) {
            NavigationBarItem(
                selected = currentRoute == Routes.PROFILE,
                onClick = {
                    if (token.isNullOrBlank()) {
                        navController.navigate(Routes.AUTH)
                    } else {
                        navController.navigate(Routes.PROFILE)
                    }
                },
                icon = { Icon(Icons.Default.Person, contentDescription = null) },
                label = {
                    Text(if (token.isNullOrBlank()) "Iniciar sesión" else "Perfil")
                }
            )
        }
    }
}
