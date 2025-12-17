package com.example.movilecibershield.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.movilecibershield.data.local.TokenDataStore
import com.example.movilecibershield.data.remote.api.order.OrderApiService
import com.example.movilecibershield.data.remote.api.product.UserApiService
import com.example.movilecibershield.data.repository.OrderRepository
import com.example.movilecibershield.data.repository.UserRepository
import com.example.movilecibershield.ui.screens.user.OrderHistoryScreen
import com.example.movilecibershield.viewmodel.UserViewModel
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Proxy

class OrderHistoryScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testRenderizaTituloHistorial() {
        val userApiProxy = Proxy.newProxyInstance(
            UserApiService::class.java.classLoader,
            arrayOf(UserApiService::class.java)
        ) { _, _, _ -> null } as UserApiService

        val orderApiProxy = Proxy.newProxyInstance(
            OrderApiService::class.java.classLoader,
            arrayOf(OrderApiService::class.java)
        ) { _, _, _ -> null } as OrderApiService

        val userRepository = UserRepository(userApiProxy)
        val orderRepository = OrderRepository(orderApiProxy)
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val tokenDataStore = TokenDataStore(context)
        
        val viewModel = UserViewModel(userRepository, orderRepository, tokenDataStore)

        composeTestRule.setContent {
            OrderHistoryScreen(
                navController = rememberNavController(),
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithText("Historial de Compras").assertIsDisplayed()
    }
}
