package com.example.movilecibershield.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movilecibershield.data.local.TokenCache
import com.example.movilecibershield.data.local.TokenDataStore
import com.example.movilecibershield.data.model.order.OrderResponse
import com.example.movilecibershield.ui.components.OrderDetailDialog
import com.example.movilecibershield.ui.components.OrderRow
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
                tokenDataStore = tokenDataStore,
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
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun OrderHistoryScreen(
            navController: NavController,
            viewModel: UserViewModel
        ) {
            val orders by viewModel.orders.collectAsState()
            val loading by viewModel.loading.collectAsState()
            var selectedOrder by remember { mutableStateOf<OrderResponse?>(null) }

            LaunchedEffect(Unit) {
                viewModel.refreshOrders()
            }

            val backgroundBrush = remember {
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF111827),
                        Color(0xFF020617)
                    )
                )
            }
            val topBarColor = Color(0xFF111827)
            selectedOrder?.let {
                OrderDetailDialog(
                    order = it,
                    onDismiss = { selectedOrder = null }
                )
            }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Historial de Compras", color = Color.White) },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Atrás",
                                    tint = Color.White
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = topBarColor
                        )
                    )
                }
            ) { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundBrush)
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.White
                        )
                    } else if (orders.isEmpty()) {
                        Text(
                            "No tienes pedidos aún.",
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.White
                        )
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(orders) {
                                OrderRow(
                                    order = it,
                                    onShowDetails = { order -> selectedOrder = order }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
