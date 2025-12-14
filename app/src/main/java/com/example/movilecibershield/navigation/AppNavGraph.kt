package com.example.movilecibershield.navigation

import ProductViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movilecibershield.data.local.TokenDataStore
import com.example.movilecibershield.ui.screens.HomeScreen
import com.example.movilecibershield.ui.screens.SplashScreen
import com.example.movilecibershield.ui.screens.auth.AuthScreen
import com.example.movilecibershield.ui.screens.user.ProfileScreen
import com.example.movilecibershield.viewmodel.AuthViewModel
import com.example.movilecibershield.viewmodel.UserViewModel


@Composable
fun AppNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    productViewModel: ProductViewModel,
    tokenDataStore: TokenDataStore
) {
    val token by tokenDataStore.getToken.collectAsState(initial = null)

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {

        composable(Routes.SPLASH) {
            SplashScreen(navController, tokenDataStore)
        }

        composable(Routes.AUTH) {
            AuthScreen(authViewModel, navController)
        }

        composable(Routes.HOME) {
            HomeScreen(
                viewModel = productViewModel,
                navController = navController,
                token = token
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                viewModel = userViewModel,
                navController = navController,
                token = token
            )
        }
    }
}







