package com.example.movilecibershield.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movilecibershield.data.local.TokenDataStore
import com.example.movilecibershield.ui.screens.HomeScreen
import com.example.movilecibershield.ui.screens.SplashScreen
import com.example.movilecibershield.ui.screens.auth.AuthScreen
import com.example.movilecibershield.viewmodel.AuthViewModel
import com.example.movilecibershield.viewmodel.ProductViewModel


@Composable
fun AppNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    productViewModel: ProductViewModel,
    tokenDataStore: TokenDataStore
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {

        composable(Routes.SPLASH) {
            SplashScreen(
                navController = navController,
                tokenDataStore = tokenDataStore
            )
        }

        composable(Routes.AUTH) {
            AuthScreen(
                viewModel = authViewModel,
                navController = navController
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                viewModel = productViewModel
            )
        }
    }
}






