package com.example.movilecibershield.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movilecibershield.ui.screens.SplashScreen
import com.example.movilecibershield.ui.screens.auth.AuthScreen
import com.example.movilecibershield.viewmodel.AuthViewModel
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.auth.AUTH

@Composable
fun AppNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {

        composable(Routes.SPLASH) {
            SplashScreen(
                navController = navController
            )
        }

        composable(Routes.AUTH) {
            AuthScreen(
                viewModel = authViewModel,
                navController = navController
            )
        }

        composable(Routes.HOME) {
            HomeScreen()
        }
    }
}

