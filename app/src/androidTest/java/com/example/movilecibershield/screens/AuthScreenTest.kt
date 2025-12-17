package com.example.movilecibershield.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.movilecibershield.data.local.TokenDataStore
import com.example.movilecibershield.data.remote.api.AuthApiService
import com.example.movilecibershield.data.repository.AuthRepository
import com.example.movilecibershield.ui.screens.auth.AuthScreen
import com.example.movilecibershield.viewmodel.AuthViewModel
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Proxy

class AuthScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testRenderizaTituloAuth() {
        val authApiProxy = Proxy.newProxyInstance(
            AuthApiService::class.java.classLoader,
            arrayOf(AuthApiService::class.java)
        ) { _, _, _ -> null } as AuthApiService

        val repository = AuthRepository(authApiProxy)
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val tokenDataStore = TokenDataStore(context)
        val viewModel = AuthViewModel(repository, tokenDataStore)

        composeTestRule.setContent {
            AuthScreen(
                viewModel = viewModel,
                navController = rememberNavController()
            )
        }

        composeTestRule.onNodeWithText("CIBERSHIELD").assertIsDisplayed()
    }
}
