package com.example.movilecibershield.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.movilecibershield.data.remote.api.order.OrderApiService
import com.example.movilecibershield.data.repository.OrderRepository
import com.example.movilecibershield.ui.screens.order.CheckoutScreen
import com.example.movilecibershield.ui.viewmodel.CartViewModel
import com.example.movilecibershield.viewmodel.CheckoutViewModel
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Proxy

class CheckoutScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testRenderizaTituloCheckout() {
        val orderApiProxy = Proxy.newProxyInstance(
            OrderApiService::class.java.classLoader,
            arrayOf(OrderApiService::class.java)
        ) { _, _, _ -> null } as OrderApiService
        
        val repository = OrderRepository(orderApiProxy)
        val cartViewModel = CartViewModel()
        val checkoutViewModel = CheckoutViewModel(repository, cartViewModel)

        composeTestRule.setContent {
            CheckoutScreen(
                navController = rememberNavController(),
                viewModel = checkoutViewModel
            )
        }

        composeTestRule.onNodeWithText("Checkout").assertIsDisplayed()
    }
}
