package com.example.movilecibershield.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.movilecibershield.data.local.TokenCache
import com.example.movilecibershield.data.local.TokenDataStore
import com.example.movilecibershield.navigation.Routes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

@Composable
fun SplashScreen(
    navController: NavHostController,
    tokenDataStore: TokenDataStore
) {
    LaunchedEffect(Unit) {
        delay(1500)

        val token = tokenDataStore.getToken().first()

        TokenCache.token = token

        val destination = if (!token.isNullOrBlank()) {
            Routes.HOME
        } else {
            Routes.AUTH
        }

        navController.navigate(destination) {
            popUpTo(Routes.SPLASH) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "CIBERSHIELD")
    }
}
