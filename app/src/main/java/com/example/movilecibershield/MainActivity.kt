package com.example.movilecibershield

import ProductViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.movilecibershield.data.local.TokenDataStore
import com.example.movilecibershield.data.remote.ConfigApi
import com.example.movilecibershield.data.repository.AuthRepository
import com.example.movilecibershield.data.repository.CheckoutRepository
import com.example.movilecibershield.data.repository.ProductRepository
import com.example.movilecibershield.data.repository.UserRepository
import com.example.movilecibershield.navigation.AppNavGraph
import com.example.movilecibershield.ui.theme.MovileCibershieldTheme
import com.example.movilecibershield.ui.viewmodel.CartViewModel
import com.example.movilecibershield.ui.viewmodel.CheckoutViewModel
import com.example.movilecibershield.ui.viewmodel.CheckoutViewModelFactory
import com.example.movilecibershield.viewmodel.AuthViewModel
import com.example.movilecibershield.viewmodel.AuthViewModelFactory
import com.example.movilecibershield.viewmodel.ProductViewModelFactory
import com.example.movilecibershield.viewmodel.UserViewModel
import com.example.movilecibershield.viewmodel.UserViewModelFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MovileCibershieldTheme {

                val navController = rememberNavController()
                val tokenDataStore = remember { TokenDataStore(applicationContext) }

                // -------- CART --------
                val cartViewModel: CartViewModel = viewModel()

                // -------- AUTH --------
                val authRepository = remember {
                    AuthRepository(
                        authApiService = ConfigApi.authApiService
                    )
                }

                val authViewModel: AuthViewModel = viewModel(
                    factory = AuthViewModelFactory(
                        authRepository = authRepository,
                        tokenDataStore = tokenDataStore
                    )
                )

                // -------- PRODUCTOS --------
                val productRepository = remember {
                    ProductRepository(
                        productApiService = ConfigApi.productApiService
                    )
                }

                val productViewModel: ProductViewModel = viewModel(
                    factory = ProductViewModelFactory(repository = productRepository)
                )

                // -------- USER --------
                val userRepository = remember {
                    UserRepository(
                        api = ConfigApi.userApiService
                    )
                }

                val userViewModel: UserViewModel = viewModel(
                    factory = UserViewModelFactory(
                        repository = userRepository
                    )
                )

                 // -------- CHECKOUT --------
                val checkoutRepository = remember {
                    CheckoutRepository(
                        checkoutService = ConfigApi.checkoutService
                    )
                }

                val checkoutViewModel: CheckoutViewModel = viewModel(
                    factory = CheckoutViewModelFactory(
                        repository = checkoutRepository,
                        cartViewModel = cartViewModel
                    )
                )

                // -------- UI --------
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavGraph(
                        navController = navController,
                        authViewModel = authViewModel,
                        productViewModel = productViewModel,
                        userViewModel = userViewModel,
                        cartViewModel = cartViewModel,
                        checkoutViewModel = checkoutViewModel,
                        tokenDataStore = tokenDataStore
                    )
                }
            }
        }
    }
}