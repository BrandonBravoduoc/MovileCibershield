package com.example.movilecibershield.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movilecibershield.data.local.TokenCache
import com.example.movilecibershield.data.local.TokenDataStore
import com.example.movilecibershield.ui.screens.HomeScreen
import com.example.movilecibershield.ui.screens.SplashScreen
import com.example.movilecibershield.ui.screens.auth.AuthScreen
import com.example.movilecibershield.ui.screens.order.CartScreen
import com.example.movilecibershield.ui.screens.order.CheckoutScreen
import com.example.movilecibershield.ui.screens.user.OrderHistoryScreen
import com.example.movilecibershield.ui.screens.user.ProfileScreen
import com.example.movilecibershield.ui.viewmodel.CartViewModel
import com.example.movilecibershield.viewmodel.AuthViewModel
import com.example.movilecibershield.viewmodel.CheckoutViewModel
import com.example.movilecibershield.viewmodel.ContactEditViewModel
import com.example.movilecibershield.viewmodel.ProductViewModel
import com.example.movilecibershield.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun AppNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    checkoutViewModel: CheckoutViewModel,
    tokenDataStore: TokenDataStore,
    contactEditViewModel: ContactEditViewModel
) {
    val scope = rememberCoroutineScope()
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
                cartViewModel = cartViewModel,
                navController = navController,
                tokenDataStore = tokenDataStore
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                viewModel = userViewModel,
                contactEditViewModel = contactEditViewModel,
                navController = navController,
                tokenDataStore = tokenDataStore, // <--- AÑADE ESTA LÍNEA
                onLogout = {
                    scope.launch {
                        tokenDataStore.clearToken()
                        tokenDataStore.clearUserId()
                    }
                    TokenCache.token = null
                    navController.navigate(Routes.AUTH) {
                        popUpTo(0)
                    }
                },
            )
        }

        composable(Routes.CART) {
            CartScreen(
                viewModel = cartViewModel,
                navController = navController,
                tokenDataStore = tokenDataStore
            )
        }

        composable(Routes.CHECKOUT) {
            CheckoutScreen(
                navController = navController,
                viewModel = checkoutViewModel
            )
        }

        composable(Routes.ORDER_HISTORY) {
            OrderHistoryScreen(
                navController = navController,
                viewModel = userViewModel
            )
        }
    }
}
