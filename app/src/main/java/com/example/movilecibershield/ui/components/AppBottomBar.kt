package com.example.movilecibershield.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.movilecibershield.navigation.Routes


@Composable
fun AppBottomBar(
    navController: NavController,
    currentRoute: String?,
    token: String?
) {
    NavigationBar {

        NavigationBarItem(
            selected = currentRoute == Routes.HOME,
            onClick = { navController.navigate(Routes.HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Inicio") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { /* TODO Carrito */ },
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
            label = { Text("Carrito") }
        )

        NavigationBarItem(
            selected = currentRoute == Routes.PROFILE,
            onClick = {
                if (token == null)
                    navController.navigate(Routes.AUTH)
                else
                    navController.navigate(Routes.PROFILE)
            },
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text(if (token == null) "Iniciar sesión" else "Perfil") }
        )
    }
}




