package com.example.movilecibershield.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.movilecibershield.data.model.product.Product
import com.example.movilecibershield.ui.screens.order.CartScreen
import com.example.movilecibershield.ui.viewmodel.CartViewModel
import org.junit.Rule
import org.junit.Test

class CartScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testCarritoVacio() {
        val viewModel = CartViewModel()

        composeTestRule.setContent {
            CartScreen(
                viewModel = viewModel,
                navController = rememberNavController(),
                token = "dummy_token"
            )
        }
        composeTestRule.onNodeWithText("Tu carrito está vacío").assertIsDisplayed()
        composeTestRule.onNodeWithText("Mi Carrito").assertIsDisplayed()
    }

    @Test
    fun testCarritoConProductos() {
        val viewModel = CartViewModel()
        val productoPrueba = Product(
            id = 1,
            nombre = "Mouse Gamer Test",
            precio = 25000.0,
            foto = "",
            categoria = "Software",
            marca = "ASUS"
        )
        viewModel.addToCart(productoPrueba)

        composeTestRule.setContent {
            CartScreen(
                viewModel = viewModel,
                navController = rememberNavController(),
                token = "dummy_token"
            )
        }
        composeTestRule.onNodeWithText("Mouse Gamer Test").assertIsDisplayed()
        composeTestRule.onNodeWithText("Pagar Ahora").assertIsDisplayed()
        composeTestRule
            .onAllNodesWithText("$ 25000", substring = true)
            .onFirst()
            .assertIsDisplayed()
    }
}