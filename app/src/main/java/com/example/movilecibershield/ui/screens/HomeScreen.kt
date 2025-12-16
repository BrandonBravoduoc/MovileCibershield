package com.example.movilecibershield.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.movilecibershield.data.local.TokenDataStore
import com.example.movilecibershield.navigation.Routes
import com.example.movilecibershield.ui.components.AppBottomBar
import com.example.movilecibershield.ui.components.ProductCard
import com.example.movilecibershield.ui.components.SearchBar
import com.example.movilecibershield.ui.viewmodel.CartViewModel
import com.example.movilecibershield.viewmodel.ProductViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    navController: NavController,
    tokenDataStore: TokenDataStore
) {
    val products = viewModel.products
    val isLoading = viewModel.isLoading
    val error = viewModel.errorMessage

    val currentRoute = Routes.HOME

    val backgroundBrush = remember {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF111827),
                Color(0xFF020617)
            )
        )
    }

    val topBarColor = Color(0xFF111827)

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(topBarColor)
                    .padding(top = 50.dp, bottom = 16.dp)
            ) {
                SearchBar(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    onSearch = { query -> viewModel.searchProducts(query) }
                )
            }
        },

        bottomBar = {
            AppBottomBar(
                navController = navController,
                currentRoute = currentRoute,
                tokenDataStore = tokenDataStore
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(backgroundBrush)
        ) {
            when {
                isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )

                error != null -> Text(
                    text = error ?: "Error desconocido",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )

                else -> LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(products) { product ->
                        ProductCard(
                            product = product,
                            onAddToCart = {
                                cartViewModel.addToCart(it)
                                println("Agregado al carrito: ${it.nombre}")
                            }
                        )
                    }
                }
            }
        }
    }
}