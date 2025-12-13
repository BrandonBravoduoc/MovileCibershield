package com.example.movilecibershield


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.movilecibershield.data.local.TokenDataStore
import com.example.movilecibershield.data.remote.ConfigApi
import com.example.movilecibershield.data.repository.AuthRepository
import com.example.movilecibershield.navigation.AppNavGraph
import com.example.movilecibershield.ui.theme.MovileCibershieldTheme
import com.example.movilecibershield.viewmodel.AuthViewModel



import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movilecibershield.viewmodel.AuthViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MovileCibershieldTheme {

                val navController = rememberNavController()

                val tokenDataStore = remember {
                    TokenDataStore(applicationContext)
                }

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

                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavGraph(
                        navController = navController,
                        authViewModel = authViewModel,
                        tokenDataStore = tokenDataStore
                    )
                }
            }
        }
    }
}




