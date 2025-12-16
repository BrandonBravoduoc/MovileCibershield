package com.example.movilecibershield.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
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
        val simpleProductViewModel = ProductViewModel(null)
        val simpleCartViewModel = CartViewModel()

        composeTestRule.setContent {
            HomeScreen(
                viewModel = simpleProductViewModel,
                cartViewModel = simpleCartViewModel,
                navController = rememberNavController(),
                token = "token_falso_para_test"
            )
        }
        composeTestRule.onNodeWithText("Buscar", substring = true).assertExists()
        composeTestRule.onNodeWithText("Inicio", substring = true).assertExists()
    }
}