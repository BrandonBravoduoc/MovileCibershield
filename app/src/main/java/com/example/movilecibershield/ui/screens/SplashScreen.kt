package com.example.movilecibershield.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.movilecibershield.data.local.TokenDataStore
import com.example.movilecibershield.navigation.Routes
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController,
    tokenDataStore: TokenDataStore
) {

    val token by tokenDataStore.getToken.collectAsState(initial = null)

    LaunchedEffect(token) {
        // ⏳ Splash temporal
        delay(1500)

        // Si hay token, navega a HOME, si no, navega a AUTH (LOGIN)
        if (token != null) {
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.SPLASH) { inclusive = true }
            }
        } else {
            navController.navigate(Routes.AUTH) {
                popUpTo(Routes.SPLASH) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "CiberShield")
    }
}


