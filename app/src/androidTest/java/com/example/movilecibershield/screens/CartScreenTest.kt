package com.example.movilecibershield.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.movilecibershield.data.local.TokenDataStore
import com.example.movilecibershield.ui.screens.order.CartScreen
import com.example.movilecibershield.ui.viewmodel.CartViewModel
import org.junit.Rule
import org.junit.Test

class CartScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testRenderizaTituloSimple() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val tokenDataStore = TokenDataStore(context)
        val viewModel = CartViewModel()

        composeTestRule.setContent {
            CartScreen(
                viewModel = viewModel,
                navController = rememberNavController(),
                tokenDataStore = tokenDataStore
            )
        }

        composeTestRule.onNodeWithText("Mi Carrito").assertIsDisplayed()
    }
}