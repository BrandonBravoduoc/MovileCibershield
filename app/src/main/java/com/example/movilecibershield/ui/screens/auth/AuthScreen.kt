package com.example.movilecibershield.ui.screens.auth

import com.example.movilecibershield.viewmodel.AuthViewModel


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.movilecibershield.navigation.Routes

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    when (viewModel.authMode) {

        AuthMode.LOGIN -> {
            LoginForm(
                isLoading = viewModel.isLoading,
                errorMessage = viewModel.errorMessage,
                onLogin = { email, password ->
                    viewModel.login(email, password)
                },
                onGoToRegister = {
                    viewModel.switchToRegister()
                }
            )
        }

        AuthMode.REGISTER -> {
            RegisterForm(
                isLoading = viewModel.isLoading,
                errorMessage = viewModel.errorMessage,
                onRegister = { registerData ->
                    viewModel.register(registerData)
                },
                onGoToLogin = {
                    viewModel.switchToLogin()
                }
            )
        }
    }

    viewModel.authResponse?.let {
        LaunchedEffect(it) {
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.SPLASH) { inclusive = true }
            }
            viewModel.clearResults()
        }
    }
}


