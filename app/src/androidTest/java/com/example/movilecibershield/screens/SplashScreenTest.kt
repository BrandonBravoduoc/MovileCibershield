package com.example.movilecibershield.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.movilecibershield.data.local.TokenDataStore
import com.example.movilecibershield.navigation.Routes
import com.example.movilecibershield.ui.screens.SplashScreen
import org.junit.Rule
import org.junit.Test

class SplashScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testSplashScreenMuestraTexto() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val tokenDataStore = TokenDataStore(context)

        composeTestRule.setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = Routes.SPLASH) {
                composable(Routes.SPLASH) {
                    SplashScreen(
                        navController = navController,
                        tokenDataStore = tokenDataStore
                    )
                }
                composable(Routes.AUTH) { }
                composable(Routes.HOME) { }
            }
        }
        composeTestRule.onNodeWithText("CIBERSHIELD").assertIsDisplayed()
    }
}