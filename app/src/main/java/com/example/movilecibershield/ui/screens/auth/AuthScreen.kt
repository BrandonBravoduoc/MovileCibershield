package com.example.movilecibershield.ui.screens.auth

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.movilecibershield.navigation.Routes
import com.example.movilecibershield.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    navController: NavHostController
) {
    val authResponse = viewModel.authResponse

    if (authResponse != null) {
        LaunchedEffect(authResponse) {
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.SPLASH) { inclusive = true }
                launchSingleTop = true
            }
            viewModel.clearResults()
        }
    }

    val authGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF111827),
            Color(0xFF020617)
        )
    )

    Scaffold(
        containerColor = Color.Transparent
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(authGradient)
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "CIBERSHIELD",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 96.dp)
            )

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Crossfade(
                    targetState = viewModel.authMode,
                    label = "AuthModeTransition"
                ) { mode ->
                    when (mode) {

                        AuthMode.LOGIN -> {
                            LoginForm(
                                isLoading = viewModel.isLoading,
                                errorMessage = viewModel.errorMessage,
                                onLogin = viewModel::login,
                                onGoToRegister = viewModel::switchToRegister,
                                onCancel = {
                                    navController.navigate(Routes.HOME) {
                                        popUpTo(Routes.AUTH) { inclusive = true }
                                    }
                                }
                            )
                        }

                        AuthMode.REGISTER -> {
                            RegisterForm(
                                isLoading = viewModel.isLoading,
                                errorMessage = viewModel.errorMessage,
                                onRegister = viewModel::register,
                                onGoToLogin = viewModel::switchToLogin
                            )
                        }
                    }
                }
            }
        }
    }
}
