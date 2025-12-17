package com.example.movilecibershield.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.movilecibershield.data.local.TokenDataStore
import com.example.movilecibershield.ui.screens.HomeScreen
import com.example.movilecibershield.ui.viewmodel.CartViewModel
import com.example.movilecibershield.viewmodel.ProductViewModel
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testSoloTextoVisible() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val tokenDataStore = TokenDataStore(context)
        val simpleProductViewModel = ProductViewModel(null)
        val simpleCartViewModel = CartViewModel()

        composeTestRule.setContent {
            HomeScreen(
                viewModel = simpleProductViewModel,
                cartViewModel = simpleCartViewModel,
                navController = rememberNavController(),
                tokenDataStore = tokenDataStore
            )
        }

        composeTestRule.onNodeWithText("Buscar", substring = true).assertExists()

    }
}